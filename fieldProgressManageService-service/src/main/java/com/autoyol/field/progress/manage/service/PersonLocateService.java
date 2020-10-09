package com.autoyol.field.progress.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.request.location.LocatePageReqVo;
import com.autoyol.field.progress.manage.response.location.LocateSelectRespVo;
import com.autoyol.field.progress.manage.response.location.LocateVo;
import com.autoyol.field.progress.manage.response.location.TackBackOrderVo;
import com.autoyol.field.progress.manage.util.http.HttpUtils;
import com.dianping.cat.Cat;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.Chinese2Letter.getFirstLetter;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.convertToLocalDateTime;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.zoneTime;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class PersonLocateService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AppUserMapper appUserMapper;

    @Resource
    private AppUserInfoMapper appUserInfoMapper;

    @Resource
    private AttendanceMapper attendanceMapper;

    @Resource
    private DictCache dictCache;

    @Resource
    private PersonLocationMapper personLocationMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    PickDeliverPickInfoLogMapper pickDeliverPickInfoLogMapper;

    @Resource
    PickDeliverDeliverInfoLogMapper pickDeliverDeliverInfoLogMapper;

    @Resource
    PickDeliverOrderInfoMapper pickDeliverOrderInfoMapper;

    @Value("${locate.max.minutes:30}")
    private Integer maxMinutes;

    @Value("${locate.map.key:84604bca54a00e85a12baefb050a842c}")
    private String mapKey;

    private static final Function<Integer, Boolean> isSoonComplete = fieldStatus -> Optional.ofNullable(fieldStatus).orElse(NEG_ZERO) >= INT_FOUR &&
            Optional.of(fieldStatus).orElse(NEG_ZERO) <= INT_SIX;

    public LocateSelectRespVo selectOrderByUser(Integer userId) throws Exception {
        LocateSelectRespVo respVo = new LocateSelectRespVo();
        AppUserEntity user = appUserMapper.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return respVo;
        }
        respVo.setUserName(user.getUserName());
        PersonLocationEntity personLocationEntity = personLocationMapper.queryByUserId(userId);
        respVo.setLocateTime(Optional.ofNullable(personLocationEntity)
                .map(locate -> Optional.ofNullable(locate.getUpdateTime()).orElse(locate.getCreateTime())).orElse(null));

        List<PickDeliverScheduleInfoEntity> tackBackOrderList = pickDeliverScheduleInfoMapper.queryByUserId(userId);
        if (CollectionUtils.isEmpty(tackBackOrderList)) {
            return respVo;
        }
        List<SysDictEntity> serverTypeList = dictCache.getDictByTypeNameFromCache(FLOW_SERVER_TYPE);

        setPickDeliverLonAndLat(respVo, tackBackOrderList);

        respVo.setList(buildTackBackOrderVo(tackBackOrderList, serverTypeList));
        return respVo;
    }

    private static PickDeliverPickInfoEntity getPick(String tackBackOrderNo, Integer serviceType){
        PickDeliverPickInfoEntity pickInfo = new PickDeliverPickInfoEntity();
        pickInfo.setPickDeliverOrderNo(tackBackOrderNo);
        pickInfo.setServiceType(serviceType);
        return pickInfo;
    }

    private static PickDeliverDeliverInfoLogEntity getDeliver(String tackBackOrderNo, Integer serviceType){
        PickDeliverDeliverInfoLogEntity deliverInfoLogEntity = new PickDeliverDeliverInfoLogEntity();
        deliverInfoLogEntity.setPickDeliverOrderNo(tackBackOrderNo);
        deliverInfoLogEntity.setServiceType(serviceType);
        return deliverInfoLogEntity;
    }

    private static PickDeliverOrderInfoEntity getOrder(String tackBackOrderNo, Integer serviceType){
        PickDeliverOrderInfoEntity order = new PickDeliverOrderInfoEntity();
        order.setPickDeliverOrderNo(tackBackOrderNo);
        order.setServiceType(serviceType);
        return order;
    }

    private void setPickDeliverLonAndLat(LocateSelectRespVo respVo, List<PickDeliverScheduleInfoEntity> tackBackOrderList) {
        PickDeliverDeliverInfoLogEntity logEntity = Optional.of(tackBackOrderList).filter(completeList -> completeList.size() > 1)
                .flatMap(completeList -> completeList.stream().map(this::buildDeliverObj)
                        .max(Comparator.comparing(PickDeliverDeliverInfoLogEntity::getDelayDeliverTime)))
                .orElseGet(() -> buildDeliverObj(tackBackOrderList.get(0)));
        respVo.setDeliverLongitude(logEntity.getChangeDeliverLong());
        respVo.setDeliverLatitude(logEntity.getChangeDeliverLat());
        PickDeliverPickInfoLogEntity pickInfoLogEntity = pickDeliverPickInfoLogMapper.selectByCond(getPick(logEntity.getPickDeliverOrderNo(), logEntity.getServiceType()));
        if (Objects.isNull(pickInfoLogEntity) || StringUtils.isEmpty(pickInfoLogEntity.getChangePickAddress())) {
            List<PickDeliverOrderInfoEntity> orderInfoList = pickDeliverOrderInfoMapper.selectByCond(getOrder(logEntity.getPickDeliverOrderNo(), logEntity.getServiceType()));
            if (CollectionUtils.isEmpty(orderInfoList)) {
                return;
            }
            PickDeliverOrderInfoEntity orderInfoEntity = orderInfoList.get(0);
            respVo.setPickLongitude(orderInfoEntity.getPickLong());
            respVo.setPickLatitude(orderInfoEntity.getPickLat());
        } else {
            respVo.setPickLongitude(pickInfoLogEntity.getChangePickLong());
            respVo.setPickLatitude(pickInfoLogEntity.getChangePickLat());
        }
    }

    private List<TackBackOrderVo> buildTackBackOrderVo(List<PickDeliverScheduleInfoEntity> tackBackOrderList, List<SysDictEntity> serverTypeList) {
        return tackBackOrderList.stream().map(schedule -> {
            TackBackOrderVo orderVo = new TackBackOrderVo();
            List<PickDeliverOrderInfoEntity> orderInfoList = pickDeliverOrderInfoMapper.selectByCond(getOrder(schedule.getPickDeliverOrderNo(), schedule.getServiceType()));
            if (CollectionUtils.isEmpty(orderInfoList)) {
                return null;
            }
            PickDeliverOrderInfoEntity orderInfoEntity = orderInfoList.get(0);
            orderVo.setServerTypeVal(getLabelByCode(serverTypeList, String.valueOf(orderInfoEntity.getServiceType())));
            PickDeliverPickInfoLogEntity pickInfoLogEntity = pickDeliverPickInfoLogMapper.selectByCond(getPick(schedule.getPickDeliverOrderNo(), schedule.getServiceType()));
            orderVo.setTackBackTime(orderInfoEntity.getPickTime());
            orderVo.setTackBackAddr(Optional.ofNullable(pickInfoLogEntity)
                    .map(pick -> Optional.ofNullable(pick.getChangePickAddress()).orElse(orderInfoEntity.getPickAddr()))
                    .orElse(orderInfoEntity.getPickAddr()));
            PickDeliverDeliverInfoLogEntity deliverInfo = pickDeliverDeliverInfoLogMapper.selectByCond(getDeliver(schedule.getPickDeliverOrderNo(), schedule.getServiceType()));
            orderVo.setExpectDeliverTime(Optional.ofNullable(deliverInfo)
                    .map(deliver -> Optional.ofNullable(deliver.getDelayDeliverTime()).orElse(orderInfoEntity.getDeliverTime()))
                    .orElse(orderInfoEntity.getDeliverTime()));
            orderVo.setDeliverAddr(Optional.ofNullable(deliverInfo)
                    .map(deliver -> Optional.ofNullable(deliver.getChangeDeliverAddress()).orElse(orderInfoEntity.getDeliverAddr()))
                    .orElse(orderInfoEntity.getDeliverAddr()));
            return orderVo;
        }).filter(Objects::nonNull).sorted(Comparator.comparing(TackBackOrderVo::getExpectDeliverTime)).limit(INT_TOW).collect(Collectors.toList());
    }

    public List<LocateVo> findLocateByPage(LocatePageReqVo reqVo) throws Exception {
        AppUserEntity userEntity = new AppUserEntity();
        userEntity.setCityId(reqVo.getCityId());
        userEntity.setCurStatus(String.valueOf(CacheConstraint.NEG_ONE));
        List<AppUserEntity> entities = appUserMapper.queryAppUserByCond(new AppUserEntity());
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.EMPTY_LIST;
        }

        List<AppUserEntity> userEntities = entities.stream()
                .filter(u -> hitListSearch(getList(reqVo.getCompanyIdStr(), CacheConstraint.SPLIT_COMMA, Integer::parseInt),
                        u.getEmploymentCompanyId(), List::contains))
                .collect(Collectors.toList());

        List<SysDictEntity> locateDictList = dictCache.getDictByTypeNameFromCache(LOCATE_STATUS);
        List<LocateVo> locateVoList = enableUserAndDistributableAndOnline(userEntities)
                .map(u -> {
                    LocateVo locateVo = new LocateVo();
                    locateVo.setUserId(u.getId());
                    locateVo.setUserName(u.getUserName());
                    locateVo.setFirstLetter(getFirstLetter(locateVo.getUserName()));

                    PersonLocationEntity personLocationEntity = personLocationMapper.queryByUserId(u.getId());
                    if (Objects.nonNull(personLocationEntity)) {
                        locateVo.setLongitude(personLocationEntity.getLongitude());
                        locateVo.setLatitude(personLocationEntity.getLatitude());
                    }

                    SysDictEntity defaultDict = new SysDictEntity();
                    defaultDict.setCode(INT_FOUR);
                    defaultDict.setLabel(NO_LOCATE);
                    SysDictEntity dictEntity = getLocateTypeAndSetLocateVo(reqVo, locateDictList, locateVo, personLocationEntity, defaultDict);
                    locateVo.setLocateStatusKey(dictEntity.getCode());
                    locateVo.setLocateStatusVal(dictEntity.getLabel());
                    return locateVo;
                }).filter(locate -> hitSearch(reqVo.getLocateStatusKey(), locate.getLocateStatusKey(), Integer::equals))
                .collect(Collectors.toList());
        if (Objects.isNull(reqVo.getLongitude()) || Objects.isNull(reqVo.getLatitude())) {
            return locateVoList.stream().sorted(Comparator.comparing(LocateVo::getFirstLetter)).collect(Collectors.toList());
        }
        List<LocateVo> noLocateList = locateVoList.stream().filter(vo -> Objects.isNull(vo.getSortDis()))
                .sorted(Comparator.comparing(LocateVo::getFirstLetter)).collect(Collectors.toList());
        List<LocateVo> locateList = locateVoList.stream().filter(vo -> Objects.nonNull(vo.getSortDis()))
                .sorted(Comparator.comparing(LocateVo::getSortDis)).collect(Collectors.toList());
        locateList.addAll(noLocateList);
        return locateList;
    }

    private SysDictEntity getLocateTypeAndSetLocateVo(LocatePageReqVo reqVo, List<SysDictEntity> locateDictList, LocateVo locateVo,
                                                      PersonLocationEntity personLocationEntity, SysDictEntity defaultDict) {
        return Optional.ofNullable(personLocationEntity).map(locate -> {

            Date locateDate = Objects.isNull(locate.getUpdateTime()) ? locate.getCreateTime() : locate.getUpdateTime();
            if (zoneTime(convertToLocalDateTime(locateDate), LocalDateTime.now()).toMinutes() > maxMinutes) {
                return getDefaultLocateDict(locateDictList, defaultDict);
            }

            List<PickDeliverScheduleInfoEntity> tackBackOrderList = pickDeliverScheduleInfoMapper.queryByUserId(locate.getUserId());
            SysDictEntity dictEntity = getDictEntity(locateDictList, defaultDict, INT_TOW);

            if (CollectionUtils.isEmpty(tackBackOrderList)) {
                return getDictEntity(locateDictList, defaultDict, INT_ONE);
            }
            List<PickDeliverScheduleInfoEntity> isCompleteList = tackBackOrderList.stream()
                    .filter(tackBack -> isSoonComplete.apply(tackBack.getFieldAppStatus()))
                    .filter(tackBack -> {
                        return tackBack.getFieldAppStatus() != INT_FOUR || (!Objects.isNull(tackBack.getUpdateTime())
                                && zoneTime(convertToLocalDateTime(tackBack.getUpdateTime()), LocalDateTime.now()).toMinutes() >= maxMinutes);
                    })
                    .collect(Collectors.toList());
            if (tackBackOrderList.size() == isCompleteList.size()) {
                dictEntity = getDictEntity(locateDictList, defaultDict, INT_THREE);
            }
            buildDistanceByCond(reqVo, locateVo, locate, isCompleteList);
            return dictEntity;
        }).orElse(getDefaultLocateDict(locateDictList, defaultDict));
    }

    private void buildDistanceByCond(LocatePageReqVo reqVo, LocateVo locateVo, PersonLocationEntity locate, List<PickDeliverScheduleInfoEntity> isCompleteList) {
        if (Objects.nonNull(reqVo.getLongitude()) && Objects.nonNull(reqVo.getLatitude())) {
            BigDecimal longitude;
            BigDecimal latitude;
            if (Objects.isNull(reqVo.getSortKey()) || reqVo.getSortKey() == NEG_ZERO) {
                longitude = locate.getLongitude();
                latitude = locate.getLatitude();
            } else {
                PickDeliverDeliverInfoLogEntity logEntity = Optional.of(isCompleteList).filter(completeList -> completeList.size() > 1)
                        .flatMap(completeList -> completeList.stream().map(this::buildDeliverObj)
                                .max(Comparator.comparing(PickDeliverDeliverInfoLogEntity::getDelayDeliverTime)))
                        .orElseGet(() -> buildDeliverObj(isCompleteList.get(0)));
                longitude = logEntity.getChangeDeliverLong();
                latitude = logEntity.getChangeDeliverLat();
            }
            setDistance(reqVo, locateVo, longitude, latitude);
        }
    }

    private PickDeliverDeliverInfoLogEntity buildDeliverObj(PickDeliverScheduleInfoEntity complete) {
        PickDeliverDeliverInfoLogEntity deliverInfoLogEntity = pickDeliverDeliverInfoLogMapper.selectByCond(getDeliver(complete.getPickDeliverOrderNo(), complete.getServiceType()));
        if (Objects.isNull(deliverInfoLogEntity) || StringUtils.isEmpty(deliverInfoLogEntity.getChangeDeliverAddress())) {
            List<PickDeliverOrderInfoEntity> orderInfoList = pickDeliverOrderInfoMapper.selectByCond(getOrder(complete.getPickDeliverOrderNo(), complete.getServiceType()));
            PickDeliverDeliverInfoLogEntity infoLogEntity = new PickDeliverDeliverInfoEntity();
            if (CollectionUtils.isEmpty(orderInfoList)) {
                return infoLogEntity;
            }
            PickDeliverOrderInfoEntity orderInfoEntity = orderInfoList.get(0);
            infoLogEntity.setPickDeliverOrderNo(orderInfoEntity.getPickDeliverOrderNo());
            infoLogEntity.setChangeDeliverLong(orderInfoEntity.getDeliverLong());
            infoLogEntity.setChangeDeliverLat(orderInfoEntity.getDeliverLat());
            infoLogEntity.setDelayDeliverTime(orderInfoEntity.getDeliverTime());
            return infoLogEntity;
        }
        return deliverInfoLogEntity;
    }

    private SysDictEntity getDictEntity(List<SysDictEntity> locateDictList, SysDictEntity defaultDict, int code) {
        return locateDictList.stream().filter(dict -> objEquals(dict.getCode(), code, Integer::equals)).findFirst()
                .orElse(getDefaultLocateDict(locateDictList, defaultDict));
    }

    private void setDistance(LocatePageReqVo reqVo, LocateVo locateVo, BigDecimal longitude, BigDecimal latitude) {
        if (Objects.nonNull(reqVo.getLongitude()) && Objects.nonNull(reqVo.getLatitude())) {
            StringBuffer buffer = getMapUrl(reqVo, longitude, latitude);
            try {
                String mapResult = HttpUtils.get(buffer.toString());
                int dis = getDistance(mapResult);
                locateVo.setSortDis(dis);
                locateVo.setDistance(division(dis, 1000) + "km");
            } catch (Exception e) {
                logger.error("***PRO*** 地图距离查询失败,reqVo={}，longitude={}，latitude={}，error:{}", reqVo, longitude, latitude, e);
                Cat.logError("地图距离查询失败{}", e);
                locateVo.setSortDis(NEG_ZERO);
                locateVo.setDistance(NEG_ZERO + "km");
            }
        }
    }

    private static int getDistance(String mapResult) {
        JSONObject route = (JSONObject) (JSON.parseObject(mapResult, Map.class).get("route"));
        List<JSONObject> paths = (List<JSONObject>) route.get("paths");
        JSONObject info = paths.get(0);
        String distanceStr = (String) info.get("distance");
        return Integer.parseInt(distanceStr);
    }

    private StringBuffer getMapUrl(LocatePageReqVo reqVo, BigDecimal longitude, BigDecimal latitude) {
        StringBuffer buffer = new StringBuffer(GD_MAP_URL);
        buffer.append("?");
        buffer.append("origin=" + reqVo.getLongitude() + SPLIT_COMMA + reqVo.getLatitude());
        buffer.append(SPLIT_AND);
        buffer.append("destination=" + longitude + SPLIT_COMMA + latitude);
        buffer.append("&output=json&key=");
        buffer.append(mapKey);
        return buffer;
    }

    private SysDictEntity getDefaultLocateDict(List<SysDictEntity> locateDictList, SysDictEntity defaultDict) {
        return locateDictList.stream().max((o1, o2) -> o1.getCode() > o2.getCode() ? INT_ONE : NEG_ONE).orElse(defaultDict);
    }

    private Stream<AppUserEntity> enableUserAndDistributableAndOnline(List<AppUserEntity> entities) {
        return entities.stream()
                .filter(u -> StringUtils.equals(u.getCurStatus(), String.valueOf(NEG_ZERO)))
                .filter(u -> {
                    AppUserInfoEntity userInfoEntity = appUserInfoMapper.selectByUserId(u.getId());
                    return INT_ONE == userInfoEntity.getDistributable();
                })
                .filter(u -> {
                    List<AttendanceEntity> attendanceEntityList = attendanceMapper.selectTodayByUserId(u.getId());
                    if (CollectionUtils.isEmpty(attendanceEntityList)) {
                        return false;
                    }
                    int isOnline = attendanceEntityList.stream().map(AttendanceEntity::getMark).reduce(Integer::sum).orElse(NEG_ONE);
                    return isOnline < INT_TOW;
                });
    }
}

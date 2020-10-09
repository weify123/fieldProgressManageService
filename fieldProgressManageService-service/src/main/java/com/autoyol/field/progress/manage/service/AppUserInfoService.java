package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.cache.UserInfoCache;
import com.autoyol.field.progress.manage.convert.ConvertAppUser;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.mapper.AppUserCertMapper;
import com.autoyol.field.progress.manage.mapper.AppUserInfoMapper;
import com.autoyol.field.progress.manage.mapper.AppUserMapper;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.request.dict.DictReqVo;
import com.autoyol.field.progress.manage.request.user.ImportUserInfoReqVo;
import com.autoyol.field.progress.manage.request.user.UserInfoSearchReqVo;
import com.autoyol.field.progress.manage.request.user.UserInfoUpdateReqVo;
import com.autoyol.field.progress.manage.response.SysDictRespVo;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoPageRespVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoVo;
import com.autoyol.field.progress.manage.util.poi.PoiUtil;
import com.autoyol.field.progress.manage.vo.UserInfoVo;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.constraint.FieldErrorCode.*;
import static com.autoyol.field.progress.manage.util.ImportLogUtil.appendLog;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static java.util.stream.IntStream.range;

@Service
public class AppUserInfoService extends AbstractService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AppUserService appUserService;

    @Resource
    CityCache cityCache;

    @Resource
    EmployCompanyMapper employCompanyMapper;

    @Resource
    DictCache dictCache;

    @Resource
    private DictService dictService;

    @Resource
    UserInfoCache userInfoCache;

    @Resource
    private AppUserInfoMapper appUserInfoMapper;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    UserCertService userCertService;

    @Resource
    CommonService commonService;

    @Resource
    private AppUserCertMapper appUserCertMapper;

    @Resource
    ImportService importService;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    @Resource
    MsgService msgService;

    public AppUserInfoPageRespVo findUserByPage(UserInfoSearchReqVo reqVo) throws Exception {
        AppUserInfoPageRespVo respVo = new AppUserInfoPageRespVo();
        respVo.setPageNo(reqVo.getPageNo());
        respVo.setPageSize(reqVo.getPageSize());
        AppUserInfoEntity userInfoEntity = ConvertAppUser.convertUserInfoToUser(reqVo);
        int count = appUserInfoMapper.countObjByCond(userInfoEntity);
        respVo.setTotal(count);
        if (count <= NEG_ZERO) {
            return respVo;
        }

        List<AppUserInfoEntity> userInfoEntities = appUserInfoMapper.queryPageObjByCond(userInfoEntity);
        final Map<String, List<SysDictRespVo>> map = dictService.findDictByName(new DictReqVo(getTypeStrBuilder(USER_STATUS, EMPLOY_TYPE, STAR_LEVEL, STATION_TYPE, DISTRIBUTE_TYPE)));
        respVo.setUserInfoList(userInfoEntities.stream()
                .filter(userInfo -> hitSearch(String.valueOf(userInfo.getStar()), reqVo.getStar(), String::contains))
                .filter(userInfo -> hitSearch(reqVo.getStation(), userInfo.getStation(), String::contains))
                .map(entity -> buildInfoRespVo(map, entity)).collect(Collectors.toList()));
        return respVo;

    }

    public AppUserInfoVo findUserInfoById(Integer userId, String newUserId) throws Exception {
        final Map<String, List<SysDictRespVo>> map = dictService.findDictByName(new DictReqVo(getTypeStrBuilder(USER_STATUS, EMPLOY_TYPE, STAR_LEVEL, STATION_TYPE, DISTRIBUTE_TYPE)));
        AppUserInfoEntity entity = appUserInfoMapper.selectByUserId(userId);
        return getObjOrDefault(entity, info -> {
            AppUserInfoVo vo = buildInfoRespVo(map, info);
            buildInfoVoPath(info, vo);
            return vo;
        }, null);

    }

    private void buildInfoVoPath(AppUserInfoEntity info, AppUserInfoVo vo) {
        List<AppUserCertEntity> certEntities = appUserCertMapper.findCertByUserId(info.getUserId());
        vo.setAvatarPath(getCertPath(certEntities, INT_THREE, INT_TOW));
        vo.setIdCardFrontPath(getCertPath(certEntities, INT_ONE, NEG_ZERO));
        vo.setIdCardBackPath(getCertPath(certEntities, INT_ONE, INT_ONE));
        vo.setDriverFrontPath(getCertPath(certEntities, INT_TOW, NEG_ZERO));
        vo.setDriverBackPath(getCertPath(certEntities, INT_TOW, INT_ONE));
    }

    private String getCertPath(List<AppUserCertEntity> certEntities, int certType, int side) {
        if (CollectionUtils.isEmpty(certEntities)) {
            return STRING_EMPTY;
        }
        return certEntities.stream().filter(cert -> objEquals(cert.getIsDelete(), NEG_ZERO, Integer::equals))
                .filter(cert -> objEquals(cert.getCertType(), certType, Integer::equals))
                .filter(cert -> objEquals(cert.getSide(), side, Integer::equals)).findFirst().orElse(new AppUserCertEntity())
                .getCertPath();
    }

    public void updateUserInfoById(UserInfoUpdateReqVo reqVo) throws Exception {

        List<UserInfoVo> vos = userInfoCache.findAllMobileAndIdNoFromCache();
        AppUserInfoEntity userInfoEntity = appUserInfoMapper.selectByUserId(reqVo.getUserId());
        if (Objects.isNull(userInfoEntity)) {
            throw new TackBackException(APP_USER_NOT_EXIST);
        }

        List<Integer> userIdList = Optional.ofNullable(vos).map(ls -> ls.stream()
                .filter(info -> StringUtils.equals(info.getIdNo(), reqVo.getIdNo()))
                .map(UserInfoVo::getUserId).collect(Collectors.toList())).orElse(null);

        List<Integer> userIds = Optional.ofNullable(userIdList).map(lst -> lst.stream()
                .filter(uId -> !objEquals(uId, userInfoEntity.getUserId(), Integer::equals))
                .collect(Collectors.toList())).orElse(null);
        AppUserEntity userEntity = appUserMapper.selectByPrimaryKey(userInfoEntity.getUserId());

        if (StringUtils.equals(userEntity.getCurStatus(), String.valueOf(INT_TOW))) {
            throw new TackBackException(USER_STATUS_DISABLE);
        }

        AppUserService.checkSupplier(userEntity, commonService, reqVo.getHandleUserNo());

        if (!CollectionUtils.isEmpty(userIds) && existField(userIds)) {
            throw new TackBackException(ID_CARD_EXIST);
        }

        userIdList = Optional.ofNullable(vos).map(ls -> ls.stream()
                .filter(info -> StringUtils.equals(info.getContactMobile(), reqVo.getContactMobile()))
                .map(UserInfoVo::getUserId).collect(Collectors.toList())).orElse(null);
        userIds = Optional.ofNullable(userIdList).map(lst -> lst.stream()
                .filter(uId -> !objEquals(uId, userInfoEntity.getUserId(), Integer::equals))
                .collect(Collectors.toList())).orElse(null);
        if (!CollectionUtils.isEmpty(userIds) && existField(userIds)) {
            throw new TackBackException(CONTACT_MOBILE_EXIST);
        }

        AppUserInfoEntity entity = convertReqToEntity(reqVo);
        entity.setUserId(reqVo.getUserId());
        entity.setEmployment(getObjOrDefault(reqVo.getEmployment(), Integer::parseInt, null));
        entity.setStar(getObjOrDefault(reqVo.getStar(), Integer::parseInt, null));
        entity.setDistributable(Integer.parseInt(Optional.ofNullable(userEntity.getCurStatus())
                .filter(status -> objEquals(status, String.valueOf(NEG_ZERO), String::equals))
                .map(status -> reqVo.getDistribute())
                .orElse(String.valueOf(NEG_ZERO))));

        if (CollectionUtils.isEmpty(reqVo.getCertList())) {
            userInfoCache.deleteMobileAndIdNoCache();
            appUserInfoMapper.updateSelectById(entity);
            return;
        }

        final Map<String, List<SysDictRespVo>> map = dictService.findDictByName(new DictReqVo(getTypeStrBuilder(CERT_TYPE, SIDE_TYPE)));
        userCertService.addOrUpdate(reqVo.getCertList(), reqVo.getHandleUser(), entity, map);
        syncAppUser(dbRenYunSwitch, cityCache, dictCache, employCompanyMapper, msgService, userEntity, entity, reqVo.getCertList(), reqVo.getHandleUser());
    }

    /**
     * 仁云同步到流程处理
     *
     * @param map
     */
    public void saveOrUpdate(Map map) throws Exception {
        List<String> typeList = buildTypeList(USER_STATUS, EMPLOY_TYPE, STATION_TYPE);
        Map<String, List<SysDictEntity>> dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
        String operator = (String) map.get("operator");
        Integer userId = (Integer) map.get("userId");
        Integer renYunUserId = (Integer) map.get("renYunUserId");
        if (Objects.isNull(userId) && Objects.isNull(renYunUserId)) {
            logger.warn("接收仁云更新车管家失败，id全为空，map {}", map);
            return;
        }
        // 车管家登录信息
        AppUserEntity userEntity = buildReceiveRenYunUserEntity(map, dictMap, userId);
        userEntity.setUserId(renYunUserId);
        List<AppUserEntity> userEntityList = appUserMapper.selectNotDisabledByCond(userEntity);
        if (CollectionUtils.isNotEmpty(userEntityList)) {
            AppUserEntity appUserEntity = userEntityList.get(0);
            userEntity.setId(appUserEntity.getId());
            userEntity.setUpdateOp(operator);
            appUserMapper.updateById(userEntity);
        } else {
            userEntity.setCreateOp(operator);
            appUserMapper.insertSelective(userEntity);
        }

        // 车管家人员信息
        AppUserInfoEntity infoEntity = buildReceiveRenYunUserInfoEntity(map, dictMap, userEntity.getId());
        AppUserInfoEntity userInfoEntity = appUserInfoMapper.selectByUserId(infoEntity.getUserId());
        if (Objects.isNull(userInfoEntity)) {
            infoEntity.setCreateOp(operator);
            appUserInfoMapper.insertSelective(infoEntity);
        } else {
            infoEntity.setUpdateOp(operator);
            appUserInfoMapper.updateSelectById(infoEntity);
        }

        // 车管家证件信息
        AppUserCertEntity certEntity = new AppUserCertEntity();
        certEntity.setUserId(infoEntity.getUserId());
        // 身份证正面
        saveOrUpdateDoc(operator, (String) map.get("idCardFacePic"), certEntity, (Integer) map.get("idCardFaceIsDelete"), NEG_ZERO, INT_ONE);
        // 身份证反面
        saveOrUpdateDoc(operator, (String) map.get("idCardBackPic"), certEntity, (Integer) map.get("idCardBackIsDelete"), INT_ONE, INT_ONE);
        // 驾驶证正面
        saveOrUpdateDoc(operator, (String) map.get("driverFacePic"), certEntity, (Integer) map.get("driverFaceIsDelete"), NEG_ZERO, INT_TOW);
        // 驾驶证反面
        saveOrUpdateDoc(operator, (String) map.get("driverBackPic"), certEntity, (Integer) map.get("driverBackIsDelete"), INT_ONE, INT_TOW);
        // 头像
        saveOrUpdateDoc(operator, (String) map.get("avatarPic"), certEntity, (Integer) map.get("avatarIsDelete"), INT_TOW, INT_THREE);

    }

    private void saveOrUpdateDoc(String operator, String certPath, AppUserCertEntity certEntity, Integer certIsDelete, int side, int certType) {
        boolean isDelete = objEquals(certIsDelete, INT_ONE, Integer::equals);
        if (isDelete) {
            certEntity.setCertType(certType);
            certEntity.setSide(side);
            certEntity.setUpdateOp(operator);
            appUserCertMapper.deleteByCond(certEntity);
        }
        if (!isDelete && StringUtils.isNotEmpty(certPath)) {
            certEntity.setCertType(certType);
            certEntity.setSide(side);
            certEntity.setCertPath(certPath);
            certEntity.setCreateOp(operator);
            appUserCertMapper.insertSelective(certEntity);
        }
    }

    private AppUserInfoEntity buildReceiveRenYunUserInfoEntity(Map map, Map<String, List<SysDictEntity>> dictMap, Integer uId) {
        AppUserInfoEntity infoEntity = new AppUserInfoEntity();
        infoEntity.setUserId(uId);
        infoEntity.setIdNo((String) map.get("idNo"));
        infoEntity.setEmail((String) map.get("email"));
        infoEntity.setContactName((String) map.get("contactName"));
        infoEntity.setContactMobile((String) map.get("contactMobile"));
        infoEntity.setAddress((String) map.get("address"));
        infoEntity.setLongitude(getObjOrDefault((String) map.get("longitude"), BigDecimal::new, null));
        infoEntity.setLatitude(getObjOrDefault((String) map.get("latitude"), BigDecimal::new, null));
        infoEntity.setStar((Integer) map.get("star"));
        infoEntity.setEmployment(getCodeByLabel(dictMap.get(EMPLOY_TYPE), (String) map.get("employment")));
        infoEntity.setStation(getStationCodeStr((String) map.get("station"), dictMap.get(STATION_TYPE)));
        infoEntity.setIdCardUploaded((Integer) map.get("idCardUploaded"));
        infoEntity.setDriverUploaded((Integer) map.get("driverUploaded"));
        infoEntity.setAvatarUploaded((Integer) map.get("avatarUploaded"));
        return infoEntity;
    }

    private AppUserEntity buildReceiveRenYunUserEntity(Map map, Map<String, List<SysDictEntity>> dictMap, Integer userId) throws Exception {
        String companyName = (String) map.get("companyName");
        String city = (String) map.get("city");
        String userStatus = (String) map.get("userStatus");

        int status = getCodeByLabel(dictMap.get(USER_STATUS), userStatus);
        if (status != NEG_ONE) {
            userStatus = Optional.of(status).filter(s -> s == INT_ONE).map(x -> String.valueOf(NEG_ZERO)).orElse(String.valueOf(INT_TOW));
        }
        AppUserEntity userEntity = new AppUserEntity();
        userEntity.setId(userId);
        userEntity.setUserName((String) map.get("userName"));
        userEntity.setMobile((String) map.get("mobile"));
        userEntity.setPassWord((String) map.get("passWord"));
        userEntity.setCurStatus(userStatus);
        if (StringUtils.isNotEmpty(companyName)) {
            EmployCompanyEntity companyEntity = employCompanyMapper.getObjByName(companyName);
            userEntity.setEmploymentCompanyId(getObjOrDefault(companyEntity, EmployCompanyEntity::getId, null));
        }
        if (StringUtils.isNotEmpty(city)) {
            int cityId = cityCache.getCityIdByCityNameFromCache(city);
            userEntity.setCityId(cityId);
        }
        return userEntity;
    }

    @Override
    public int importObj(Workbook book, String operator) {

        List<ImportUserInfoReqVo> importUserList = Lists.newArrayList();
        range(NEG_ZERO, book.getNumberOfSheets()).forEach(i -> {
            Sheet sheet = book.getSheetAt(i);
            for (int j = NEG_ZERO; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                ImportUserInfoReqVo userInfo = new ImportUserInfoReqVo();
                try {
                    userInfo.setUserId(Integer.parseInt(PoiUtil.getValue(row.getCell(0))));
                } catch (NumberFormatException e) {
                    continue;
                }

                AppUserVo userRespVo = null;
                try {
                    userRespVo = appUserService.findUserById(userInfo.getUserId(), operator.split(SPLIT_COMMA)[1]);
                } catch (Exception e) {
                    logger.error("findUserById 异常 e {}, userId {}, operator {}", e, userInfo.getUserId(), operator.split(SPLIT_COMMA)[1]);
                    break;
                }

                String distribute = PoiUtil.getValue(row.getCell(17));
                List<SysDictEntity> dictList;

                try {
                    dictList = dictCache.getDictByTypeNameFromCache(DISTRIBUTE_TYPE);
                    SysDictEntity dictEntity = Optional.ofNullable(dictList)
                            .map(dict -> dict.stream().filter(d -> d.getLabel().equals(distribute)).findFirst().orElse(new SysDictEntity()))
                            .orElse(new SysDictEntity());
                    userInfo.setDistribute(dictEntity.getCode() + STRING_EMPTY);
                    // 匹配用户字段并拼接日志
                    if (matchUser(matchFail, j, row, userInfo, userRespVo, matchFailCount, userInfo.getDistribute())) {
                        continue;
                    }
                    matchSucceedCount.incrementAndGet();

                    // 校验身份证、联系人、联系人手机
                    if (validIdNoAndContactNameAndMobile(importFail, importFailCount, j, row, userInfo)) {
                        continue;
                    }

                    // 设置任职情况
                    List<SysDictEntity> dictEmploy = dictCache.getDictByTypeNameFromCache(EMPLOY_TYPE);
                    userInfo.setEmployment(PoiUtil.getValue(row.getCell(8)));
                    SysDictEntity employDict = Optional.ofNullable(dictEmploy).map(employs -> employs.stream()
                            .filter(employ -> objEquals(employ.getLabel(), userInfo.getEmployment(), String::equals))
                            .findFirst().orElse(new SysDictEntity())
                    ).orElse(new SysDictEntity());
                    if (Objects.isNull(employDict.getCode())) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    userInfo.setEmployment(employDict.getCode() + STRING_EMPTY);

                    userInfo.setAddress(getObjOrDefault(PoiUtil.getValue(row.getCell(9)), Function.identity(), null));
                    if (StringUtils.length(userInfo.getAddress()) > 100) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }

                    // 设置星级
                    List<SysDictEntity> dictStar = dictCache.getDictByTypeNameFromCache(STAR_LEVEL);
                    userInfo.setStar(PoiUtil.getValue(row.getCell(12)));
                    SysDictEntity starDict = Optional.ofNullable(dictStar).map(stars -> stars.stream()
                            .filter(star -> objEquals(star.getLabel(), userInfo.getStar(), String::equals))
                            .findFirst().orElse(new SysDictEntity())
                    ).orElse(new SysDictEntity());
                    if (Objects.isNull(starDict.getCode())) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    userInfo.setStar(starDict.getCode() + STRING_EMPTY);

                    // 设置岗位
                    List<SysDictEntity> dictStation = dictCache.getDictByTypeNameFromCache(STATION_TYPE);
                    userInfo.setStation(PoiUtil.getValue(row.getCell(13)));
                    if (StringUtils.isBlank(userInfo.getStation())) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    List<String> stringList = Stream.of(userInfo.getStation().split(SPLIT_AND)).collect(Collectors.toList());
                    List<Integer> stationList = Optional.ofNullable(dictStation).map(stations -> stations.stream()
                            .filter(station -> objEquals(station.getLabel(), stringList, (s, list) -> list.contains(s)))
                            .map(SysDictEntity::getCode)
                            .collect(Collectors.toList())
                    ).orElse(null);
                    if (CollectionUtils.isEmpty(stationList)) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    userInfo.setStation(stationList.stream()
                            .map(String::valueOf).reduce((x, y) -> x + SPLIT_COMMA + y).orElse(STRING_EMPTY));
                } catch (Exception e) {
                    logger.error("dictCache 查询异常 {}", e);
                    Cat.logError("dictCache 查询异常 {}", e);
                    break;
                }
                importSucceedCount.incrementAndGet();
                importUserList.add(userInfo);
            }
        });
        // 空白表
        List<ImportUserInfoReqVo> distinctList = importUserList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctList) && isAllLessThanZero(matchFailCount, matchSucceedCount, importFailCount, importSucceedCount)) {
            return NEG_ONE;
        }
        // 有不同的联系人等属性，直接结束
        if (distinctField(distinctList).size() < distinctList.size()) {
            return NEG_TOW;
        }

        AtomicInteger mobileCount = getMultiCount(distinctList.stream().map(ImportUserInfoReqVo::getContactMobile));
        if (mobileCount.get() > NEG_ZERO) {
            return NEG_FIVE;
        }

        AtomicInteger idNoCount = getMultiCount(distinctList.stream().map(ImportUserInfoReqVo::getIdNo));
        if (idNoCount.get() > NEG_ZERO) {
            return NEG_SIX;
        }

        if (matchFailCount.get() + importFailCount.get() > 0 && importSucceedCount.get() > 0) {
            count = NEG_THREE;
        }

        if (matchFailCount.get() + importFailCount.get() > 0 && importSucceedCount.get() <= 0) {
            count = NEG_FOUR;
        }

        matchSucceed.append(matchSucceedCount.get()).append(CH_COUNT);
        importSucceed.append(importSucceedCount.get()).append(CH_COUNT);
        // 保存日志数据库
        importService.saveImportLog(matchSucceed.toString(), matchFailCount, matchFail,
                importSucceed.toString(), importFailCount, importFail, operator.split(SPLIT_COMMA)[0],
                Integer.parseInt(CommonEnum.LOG_TYPE_USER_INFO.getCode()));

        if (CollectionUtils.isEmpty(distinctList)) {
            return count;
        }
        logger.info("***PRO***准备保存导入用户详细数据 {}", distinctList.size());
        userInfoCache.deleteMobileAndIdNoCache();
        Map<Boolean, List<AppUserInfoEntity>> listMap = classifiedData(distinctList, operator.split(SPLIT_COMMA)[0]);
        importService.userInfoImport(listMap);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            listMap.forEach((k, v) -> {
                v.forEach(entity -> {
                    AppUserEntity userEntity = appUserMapper.selectByPrimaryKey(entity.getUserId());
                    syncAppUser(dbRenYunSwitch, cityCache, dictCache, employCompanyMapper, msgService, userEntity, entity, null, operator);
                });
            });
        }
        return count;
    }

    private Map<Boolean, List<AppUserInfoEntity>> classifiedData(List<ImportUserInfoReqVo> distinctList, String operator) {

        List<AppUserInfoEntity> infoEntities = appUserInfoMapper.queryExistUserId(distinctList.stream()
                .map(ImportUserInfoReqVo::getUserId).collect(Collectors.toList()));

        if (CollectionUtils.isEmpty(infoEntities)) {
            Map<Boolean, List<AppUserInfoEntity>> map = Maps.newHashMap();
            map.put(Boolean.TRUE, distinctList.stream().map(importUserInfoReqVo -> {
                AppUserInfoEntity entity = convertImportToUserInfoEntity(importUserInfoReqVo);
                entity.setCreateOp(operator);
                return entity;
            }).collect(Collectors.toList()));
            return map;
        }
        return distinctList.stream().map(info -> {
            AppUserInfoEntity entity = convertImportToUserInfoEntity(info);
            AppUserInfoEntity tmp = infoEntities.stream()
                    .filter(infoEntity -> objEquals(infoEntity.getUserId(), info.getUserId(), Integer::equals))
                    .findFirst().orElse(null);
            if (Objects.isNull(tmp)) {
                entity.setCreateOp(operator);
                return entity;
            }
            entity.setUpdateOp(operator);
            entity.setUpdateTime(new Date());
            entity.setUserId(tmp.getUserId());
            return entity;
        }).collect(Collectors.groupingBy(entity -> Objects.isNull(entity.getId())));
    }

    private AppUserInfoEntity convertImportToUserInfoEntity(ImportUserInfoReqVo importInfo) {
        AppUserInfoEntity entity = new AppUserInfoEntity();
        BeanUtils.copyProperties(importInfo, entity);
        entity.setEmployment(getObjOrDefault(importInfo.getEmployment(), Integer::parseInt, NEG_ZERO));
        entity.setStar(getObjOrDefault(importInfo.getStar(), Integer::parseInt, NEG_ZERO));
        entity.setDistributable(getObjOrDefault(importInfo.getDistribute(), Integer::parseInt, NEG_ZERO));
        return entity;
    }

    private boolean validIdNoAndContactNameAndMobile(StringBuffer importFail, AtomicInteger importFailCount, int j, Row row, ImportUserInfoReqVo userInfo) throws Exception {
        userInfo.setIdNo(PoiUtil.getValue(row.getCell(5)));
        if (!Pattern.matches(ID_REG, userInfo.getIdNo())) {
            appendLog(importFail, importFailCount, j);
            return true;
        }

        List<UserInfoVo> vos = userInfoCache.findAllMobileAndIdNoFromCache();
        List<Integer> userIdList = Optional.ofNullable(vos).map(ls -> ls.stream()
                .filter(info -> StringUtils.equals(info.getIdNo(), userInfo.getIdNo()))
                .map(UserInfoVo::getUserId).collect(Collectors.toList())).orElse(null);

        List<Integer> userIds = Optional.ofNullable(userIdList).map(lst -> lst.stream()
                .filter(uId -> !objEquals(uId, userInfo.getUserId(), Integer::equals))
                .collect(Collectors.toList())).orElse(null);

        if (!CollectionUtils.isEmpty(userIds) && existField(userIds)) {
            appendLog(importFail, importFailCount, j);
            return true;
        }

        userInfo.setContactName(PoiUtil.getValue(row.getCell(6)));
        if (StringUtils.length(userInfo.getContactName()) > 10) {
            appendLog(importFail, importFailCount, j);
            return true;
        }

        userInfo.setContactMobile(PoiUtil.getValue(row.getCell(7)));
        if (!Pattern.matches(PHONE_REG, userInfo.getContactMobile())) {
            appendLog(importFail, importFailCount, j);
            return true;
        }
        userIdList = Optional.ofNullable(vos).map(ls -> ls.stream()
                .filter(info -> StringUtils.equals(info.getContactMobile(), userInfo.getContactMobile()))
                .map(UserInfoVo::getUserId).collect(Collectors.toList())).orElse(null);
        userIds = Optional.ofNullable(userIdList).map(lst -> lst.stream()
                .filter(uId -> !objEquals(uId, userInfo.getUserId(), Integer::equals))
                .collect(Collectors.toList())).orElse(null);
        if (!CollectionUtils.isEmpty(userIds) && existField(userIds)) {
            appendLog(importFail, importFailCount, j);
            return true;
        }

        return false;
    }

    private ArrayList<ImportUserInfoReqVo> distinctField(List<ImportUserInfoReqVo> distinctList) {
        return distinctList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(o ->
                        o.getUserId() + SPLIT_AND + o.getUserName() + SPLIT_AND + o.getMobile() + SPLIT_AND +
                                o.getCompanyName() + SPLIT_AND + o.getCityName()))), ArrayList::new));
    }

    private boolean matchUser(StringBuffer matchFail, int j, Row row,
                              ImportUserInfoReqVo userInfo, AppUserVo userRespVo,
                              AtomicInteger matchFailCount, String distribute) {
        if (Objects.isNull(userRespVo)) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }

        if (userRespVo.getStatusKey().equals(INT_TOW)) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }

        if (userRespVo.getStatusKey().equals(INT_ONE) &&
                String.valueOf(INT_ONE).equals(distribute)) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }

        userInfo.setUserName(PoiUtil.getValue(row.getCell(1)));
        if (!objEquals(userRespVo.getUserName(), userInfo.getUserName(), String::equals)) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }

        userInfo.setCompanyName(PoiUtil.getValue(row.getCell(2)));
        if (StringUtils.isBlank(userInfo.getCompanyName())) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }
        try {
            EmployCompanyEntity companyEntity = employCompanyMapper.getObjByName(userInfo.getCompanyName());
            if (Objects.isNull(companyEntity) || !objEquals(userRespVo.getCompanyId(), companyEntity.getId(), Integer::equals)) {
                appendLog(matchFail, matchFailCount, j);
                return true;
            }
            userInfo.setCompanyName(getObjOrDefault(companyEntity.getId(), String::valueOf, STRING_EMPTY));
        } catch (Exception e) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }

        userInfo.setCityName(PoiUtil.getValue(row.getCell(3)));
        try {
            Integer cityId = cityCache.getCityIdByCityNameFromCache(userInfo.getCityName());
            if (!objEquals(userRespVo.getCityId(), cityId, Integer::equals)) {
                appendLog(matchFail, matchFailCount, j);
                return true;
            }
            userInfo.setCityName(getObjOrDefault(cityId, String::valueOf, STRING_EMPTY));
        } catch (Exception e) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }

        userInfo.setMobile(PoiUtil.getValue(row.getCell(4)));
        if (!objEquals(userRespVo.getMobile(), userInfo.getMobile(), String::equals)) {
            appendLog(matchFail, matchFailCount, j);
            return true;
        }
        return false;
    }

    private boolean existField(List<Integer> userIds) throws Exception {
        if (!CollectionUtils.isEmpty(userIds)) {
            return userIds.stream().anyMatch(userId -> {
                try {
                    AppUserEntity userEntity = appUserMapper.selectByPrimaryKey(userId);
                    return Integer.parseInt(userEntity.getCurStatus()) < INT_TOW;
                } catch (Exception e) {
                    logger.error("***PRO*** existField getAppUserByIdFromCache error {}", e);
                    return false;
                }
            });
        }
        return false;
    }

    private AppUserInfoEntity convertReqToEntity(UserInfoUpdateReqVo reqVo) {
        AppUserInfoEntity entity = new AppUserInfoEntity();
        BeanUtils.copyProperties(reqVo, entity);
        entity.setUpdateOp(reqVo.getHandleUser());
        return entity;
    }

    private AppUserInfoVo buildInfoRespVo(Map<String, List<SysDictRespVo>> map, AppUserInfoEntity entity) {
        AppUserInfoVo infoRespVo = new AppUserInfoVo();

        infoRespVo.setUserName(entity.getUserName());
        infoRespVo.setMobile(entity.getUserMobile());
        infoRespVo.setRole(entity.getRole());
        infoRespVo.setStatusKey(entity.getStatusKey());
        infoRespVo.setStatusVal(getTypeValue(entity.getStatusKey(), USER_STATUS, map));
        if (Objects.nonNull(entity.getCompanyId())) {
            EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(entity.getCompanyId());
            infoRespVo.setCompanyNameFirst(getObjOrDefault(companyEntity, EmployCompanyEntity::getFirstCategory, null));
            infoRespVo.setCompanyName(getObjOrDefault(companyEntity, EmployCompanyEntity::getSecondCategory, null));
        }
        if (Objects.nonNull(entity.getCityId())) {
            try {
                infoRespVo.setCityName(getObjOrDefault(cityCache.getCityByIdFromCache(entity.getCityId()), CityLevelEntity::getCity, null));
            } catch (Exception e) {
                logger.error("***PRO*** 用户详细信息查询城市错误cityId [{}] e {}", entity.getCityId(), e);
            }
        }
        BeanUtils.copyProperties(entity, infoRespVo);
        infoRespVo.setUserId(entity.getUserId());
        infoRespVo.setEmploymentKey(entity.getEmployment());
        infoRespVo.setEmploymentVal(getTypeValue(entity.getEmployment(), EMPLOY_TYPE, map));
        infoRespVo.setStarKey(entity.getStar());
        infoRespVo.setStarVal(getTypeValue(entity.getStar(), STAR_LEVEL, map));
        infoRespVo.setDistributeKey(entity.getDistributable());
        infoRespVo.setDistributeVal(getTypeValue(entity.getDistributable(), DISTRIBUTE_TYPE, map));

        infoRespVo.setStationKey(entity.getStation());
        infoRespVo.setStationVal(getStationStr(map, entity.getStation()));
        return infoRespVo;
    }

}

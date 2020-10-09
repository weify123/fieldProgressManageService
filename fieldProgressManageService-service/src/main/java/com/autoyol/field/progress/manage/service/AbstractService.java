package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.request.cert.AppUserCertReqVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

public abstract class AbstractService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected int count = NEG_ZERO;
    protected StringBuffer matchSucceed = new StringBuffer(CH_SUCCESS);
    protected StringBuffer matchFail = new StringBuffer();
    protected StringBuffer importSucceed = new StringBuffer(CH_SUCCESS);
    protected StringBuffer importFail = new StringBuffer();

    protected AtomicInteger matchSucceedCount = new AtomicInteger(NEG_ZERO);
    protected AtomicInteger matchFailCount = new AtomicInteger(NEG_ZERO);
    protected AtomicInteger importSucceedCount = new AtomicInteger(NEG_ZERO);
    protected AtomicInteger importFailCount = new AtomicInteger(NEG_ZERO);

    public abstract int importObj(Workbook book, String operator) throws Exception;

    protected void syncAppUser(String dbRenYunSwitch,
                               CityCache cityCache, DictCache dictCache, EmployCompanyMapper employCompanyMapper, MsgService msgService,
                               AppUserEntity userEntity, AppUserInfoEntity entity, List<AppUserCertReqVo> certList, String operator) {

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(userEntity.getEmploymentCompanyId());
            CityLevelEntity cityEntity;
            Map<String, List<SysDictEntity>> dictMap;
            try {
                cityEntity = cityCache.getCityByIdFromCache(userEntity.getCityId());
                List<String> typeList = buildTypeList(USER_STATUS, EMPLOY_TYPE, STATION_TYPE);
                dictMap = dictCache.getDictByTypeNameStrFromCache(typeList);
            } catch (Exception e) {
                logger.error("***PRO*** 车管家同步仁云查询失败");
                return;
            }
            Map<String, Object> map = msgService.buildMap();
            map.put("userId", userEntity.getId());
            map.put("renYunUserId", userEntity.getUserId());
            map.put("userName", userEntity.getUserName());
            map.put("mobile", userEntity.getMobile());
            map.put("passWord", userEntity.getPassWord());
            map.put("companyName", companyEntity.getSecondCategory());
            map.put("city", cityEntity.getCity());
            String convertStatus = Optional.of(userEntity.getCurStatus())
                    .filter(s -> objEquals(s, String.valueOf(INT_TOW), String::equals))
                    .map(x -> String.valueOf(INT_ONE)).orElse(userEntity.getCurStatus());
            map.put("userStatus", getLabelByCode(dictMap.get(USER_STATUS), convertStatus));

            map.put("idNo", getObjOrDefault(entity, AppUserInfoEntity::getIdNo, null));
            map.put("email", getObjOrDefault(entity, AppUserInfoEntity::getEmail, null));
            map.put("contactName", getObjOrDefault(entity, AppUserInfoEntity::getContactName, null));
            map.put("contactMobile", getObjOrDefault(entity, AppUserInfoEntity::getContactMobile, null));
            map.put("address", getObjOrDefault(entity, AppUserInfoEntity::getAddress, null));
            map.put("longitude", getObjOrDefault(entity, AppUserInfoEntity::getLongitude, null));
            map.put("latitude", getObjOrDefault(entity, AppUserInfoEntity::getLatitude, null));
            map.put("star", getObjOrDefault(entity, AppUserInfoEntity::getStar, null));
            map.put("employment", getObjOrDefault(entity, a -> getLabelByCode(dictMap.get(EMPLOY_TYPE), String.valueOf(a.getEmployment())), null));
            map.put("station", getObjOrDefault(entity, a -> getStationStr(dictMap, a.getStation()), null));

            map.put("idCardUploaded", getObjOrDefault(entity, AppUserInfoEntity::getIdCardUploaded, null));
            map.put("driverUploaded", getObjOrDefault(entity, AppUserInfoEntity::getDriverUploaded, null));
            map.put("avatarUploaded", getObjOrDefault(entity, AppUserInfoEntity::getAvatarUploaded, null));

            map.put("idCardFacePic", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_ONE, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), NEG_ZERO, Integer::equals))
                            .findFirst().orElse(null),
                    null));
            map.put("idCardBackPic", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_ONE, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), INT_ONE, Integer::equals))
                            .findFirst().orElse(null),
                    null));
            map.put("driverFacePic", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_TOW, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), NEG_ZERO, Integer::equals))
                            .findFirst().orElse(null),
                    null));
            map.put("driverBackPic", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_TOW, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), INT_ONE, Integer::equals))
                            .findFirst().orElse(null),
                    null));
            map.put("avatarPic", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_THREE, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), INT_TOW, Integer::equals))
                            .findFirst().orElse(null),
                    null));
            // 是否删除图片
            map.put("idCardFaceIsDelete", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_ONE, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), NEG_ZERO, Integer::equals))
                            .filter(x -> StringUtils.isEmpty(x.getCertPath()))
                            .map(x -> INT_ONE)
                            .findFirst().orElse(NEG_ZERO),
                    NEG_ZERO));
            map.put("idCardBackIsDelete", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_ONE, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), INT_ONE, Integer::equals))
                            .filter(x -> StringUtils.isEmpty(x.getCertPath()))
                            .map(x -> INT_ONE)
                            .findFirst().orElse(NEG_ZERO),
                    NEG_ZERO));

            map.put("driverFaceIsDelete", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_TOW, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), NEG_ZERO, Integer::equals))
                            .filter(x -> StringUtils.isEmpty(x.getCertPath()))
                            .map(x -> INT_ONE)
                            .findFirst().orElse(NEG_ZERO),
                    NEG_ZERO));
            map.put("driverBackIsDelete", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_TOW, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), INT_ONE, Integer::equals))
                            .filter(x -> StringUtils.isEmpty(x.getCertPath()))
                            .map(x -> INT_ONE)
                            .findFirst().orElse(NEG_ZERO),
                    NEG_ZERO));
            map.put("avatarIsDelete", getObjOrDefault(certList, list -> list.stream()
                            .filter(x -> objEquals(x.getCertType(), INT_THREE, Integer::equals))
                            .filter(x -> objEquals(x.getSide(), INT_TOW, Integer::equals))
                            .filter(x -> StringUtils.isEmpty(x.getCertPath()))
                            .map(x -> INT_ONE)
                            .findFirst().orElse(NEG_ZERO),
                    NEG_ZERO));

            map.put("operator", operator);
            msgService.sendMq(sqlServer_app_user_mq_exchange, sqlServer_app_user_mq_routing_key, INT_FOUR, map);
        }
    }

    private static String getStationStr(Map<String, List<SysDictEntity>> map, String station) {
        if (StringUtils.contains(station, SPLIT_AND)) {
            return Optional.of(station)
                    .map(stationTmp -> Arrays.asList(station.split(SPLIT_AND)))
                    .map(list -> {
                        StringBuilder builder = new StringBuilder();
                        list.forEach(s -> {
                            builder.append(getTypeValue(Integer.parseInt(s), map));
                            builder.append(SPLIT_AND);
                        });
                        return builder.toString().substring(0, builder.toString().length() - 1);
                    }).orElse(STRING_EMPTY);

        } else {
            return StringUtils.isBlank(station) ? STRING_EMPTY : getTypeValue(Integer.parseInt(station), map);
        }
    }

    private static <C> String getTypeValue(final C code, final Map<String, List<SysDictEntity>> map) {
        return getObjOrDefault(map.get(STATION_TYPE), ls -> ls.stream().filter(dict -> dict.getCode().equals(code)).findFirst()
                .orElse(new SysDictEntity())
                .getLabel(), STRING_EMPTY);
    }
}

package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.UserInfoCache;
import com.autoyol.field.progress.manage.entity.AppUserCertEntity;
import com.autoyol.field.progress.manage.entity.AppUserInfoEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.AppUserCertMapper;
import com.autoyol.field.progress.manage.mapper.AppUserInfoMapper;
import com.autoyol.field.progress.manage.request.cert.AppUserCertReqVo;
import com.autoyol.field.progress.manage.response.SysDictRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.objEquals;

@Service
public class UserCertService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AppUserInfoMapper appUserInfoMapper;

    @Resource
    private AppUserCertMapper appUserCertMapper;

    @Resource
    UserInfoCache userInfoCache;

    @Transactional
    public int addOrUpdate(List<AppUserCertReqVo> certList, String operator, AppUserInfoEntity entity, Map<String, List<SysDictRespVo>> map) {
        AppUserInfoEntity dbInfo = appUserInfoMapper.selectByUserId(entity.getUserId());

        List<SysDictRespVo> dictList = map.get(CERT_TYPE);
        if (CollectionUtils.isEmpty(dictList)) {
            return NEG_ZERO;
        }

        entity.setUpdateOp(operator);
        setUserInfoUploaded(certList, entity);
        appUserInfoMapper.updateSelectById(entity);

        int count = NEG_ZERO;
        for (AppUserCertReqVo certReqVo : certList) {
            AppUserCertEntity certEntity = buildCertEntity(certReqVo, dbInfo.getUserId(), dictList, operator);
            if (Objects.isNull(certEntity)) {
                continue;
            }
            List<AppUserCertEntity> certEntities = appUserCertMapper.findCertByUserId(certEntity.getUserId());

            if (CollectionUtils.isEmpty(certEntities)) {
                if (!StringUtils.isEmpty(certEntity.getCertPath())) {
                    count += appUserCertMapper.insertSelective(certEntity);
                }
                continue;
            }

            AppUserCertEntity existCert = getExistCert(certEntity, certEntities);
            if (Objects.isNull(existCert)) {
                if (!StringUtils.isEmpty(certEntity.getCertPath())) {
                    count += appUserCertMapper.insertSelective(certEntity);
                }
                continue;
            }

            if (!existCert.getCertPath().equalsIgnoreCase(certEntity.getCertPath())) {
                appUserCertMapper.deleteSelectById(existCert);
                if (!StringUtils.isEmpty(certEntity.getCertPath())) {
                    count += appUserCertMapper.insertSelective(certEntity);
                }
            }
        }
        userInfoCache.deleteMobileAndIdNoCache();
        return count;

    }

    private void setUserInfoUploaded(List<AppUserCertReqVo> certList, AppUserInfoEntity userInfoEntity) {
        long countId = certList.stream().filter(cert -> objEquals(cert.getCertType(), INT_ONE, Integer::equals))
                .filter(cert -> !StringUtils.isEmpty(cert.getCertPath())).count();

        long countDriver = certList.stream().filter(cert -> objEquals(cert.getCertType(), INT_TOW, Integer::equals))
                .filter(cert -> !StringUtils.isEmpty(cert.getCertPath())).count();

        certList.forEach(certReq -> {
            CommonEnum commonEnum = CommonEnum.CERT_UPLOADED_YES;
            if (certReq.getCertType() == INT_ONE && countId <= INT_ONE) {
                commonEnum = CommonEnum.CERT_UPLOADED_NO;
            }
            if (certReq.getCertType() == INT_TOW && countDriver <= INT_ONE) {
                commonEnum = CommonEnum.CERT_UPLOADED_NO;
            }
            if (certReq.getCertType() == INT_THREE && StringUtils.isEmpty(certReq.getCertPath())) {
                commonEnum = CommonEnum.CERT_UPLOADED_NO;
            }
            resetUerInfoUploaded(userInfoEntity, certReq.getCertType(), commonEnum);
        });
    }

    private void resetUerInfoUploaded(AppUserInfoEntity entity, Integer certType, CommonEnum certUploadedNo) {
        if (objEquals(certType, INT_ONE, Integer::equals)) {
            entity.setIdCardUploaded(Integer.parseInt(certUploadedNo.getCode()));
        }
        if (objEquals(certType, INT_TOW, Integer::equals)) {
            entity.setDriverUploaded(Integer.parseInt(certUploadedNo.getCode()));
        }
        if (objEquals(certType, INT_THREE, Integer::equals)) {
            entity.setAvatarUploaded(Integer.parseInt(certUploadedNo.getCode()));
        }
    }

    private AppUserCertEntity getExistCert(AppUserCertEntity certEntity, List<AppUserCertEntity> certEntities) {
        return certEntities.stream()
                .filter(cert -> objEquals(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), cert.getIsDelete(), Integer::equals))
                .filter(cert -> objEquals(certEntity.getCertType(), cert.getCertType(), Integer::equals))
                .filter(cert -> objEquals(certEntity.getSide(), cert.getSide(), Integer::equals))
                .findFirst().orElse(null);
    }

    private AppUserCertEntity buildCertEntity(AppUserCertReqVo certReqVo, Integer userId, List<SysDictRespVo> dictList, String operator) {
        SysDictRespVo dictRespVo = dictList.stream().filter(dict -> dict.getCode().equals(certReqVo.getCertType())).findFirst().orElse(null);
        if (Objects.isNull(dictRespVo)) {
            return null;
        }

        AppUserCertEntity certEntity = new AppUserCertEntity();
        certEntity.setUserId(userId);
        certEntity.setCertType(certReqVo.getCertType());
        certEntity.setCertPath(certReqVo.getCertPath());
        certEntity.setSide(certReqVo.getSide());
        certEntity.setCreateOp(operator);
        certEntity.setUpdateOp(operator);
        return certEntity;
    }
}

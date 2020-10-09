package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.mapper.PickDeliverScheduleInfoMapper;
import com.autoyol.field.progress.manage.mapper.SupplierAccountMapper;
import com.autoyol.field.progress.manage.mapper.TransOrderInfoMapper;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class CommonService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CityCache cityCache;

    @Resource
    EmployCompanyMapper employCompanyMapper;

    @Resource
    SupplierAccountMapper supplierAccountMapper;

    @Resource
    PickDeliverScheduleInfoMapper pickDeliverScheduleInfoMapper;

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    public boolean checkSupplierData(Integer handleUserNo, String tackBackNo, Integer serverType, String orderNo) {
        if (Objects.nonNull(handleUserNo)) {
            SupplierAccountEntity accountEntity = getSupplierAccountByUserId(String.valueOf(handleUserNo));
            if (Objects.nonNull(accountEntity)) {
                PickDeliverScheduleInfoEntity entity = new PickDeliverScheduleInfoEntity();
                entity.setPickDeliverOrderNo(tackBackNo);
                entity.setServiceType(serverType);
                PickDeliverScheduleInfoEntity scheduleInfoEntity = pickDeliverScheduleInfoMapper.selectByCond(entity);
                if (!objEquals(scheduleInfoEntity.getSupplierCompanyId(), accountEntity.getCompanyId(), Integer::equals)) {
                    return true;
                }
                TransOrderInfoEntity transOrderInfoEntity = transOrderInfoMapper.queryByOrderNo(orderNo);
                Integer belongCity = Optional.ofNullable(transOrderInfoEntity.getBelongCity()).map(city -> {
                    try {
                        return cityCache.getCityIdByCityNameFromCache(transOrderInfoEntity.getBelongCity());
                    } catch (Exception e) {
                        logger.error("查询城市id失败 e={}", e);
                        return null;
                    }
                }).orElse(null);
                return !objEquals(getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt), belongCity, List::contains);
            }
        }
        return false;
    }

    public SupplierAccountEntity getSupplierAccountByUserId(String userId) {
        SupplierAccountEntity accountEntity = supplierAccountMapper.selectSupplierByUserId(userId);
        if (Objects.nonNull(accountEntity)) {
            accountEntity.setMannageCity(getObjOrDefault(accountEntity.getMannageCity(),
                    city -> getList(city, SPLIT_DAWN, String::valueOf).stream()
                            .reduce((x, y) -> x + SPLIT_COMMA + y)
                            .orElse(STRING_EMPTY),
                    STRING_EMPTY));
        }
        return accountEntity;
    }

    public String getCityName(Integer cityId) {
        return Optional.ofNullable(cityId).map(id -> {
            try {
                return cityCache.getCityByIdFromCache(id);
            } catch (Exception e) {
                logger.error("***PRO*** 查询城市失败,cityId=[{}]，error:{}", id, e);
                Cat.logError("查询城市失败 {}", e);
                return null;
            }
        }).map(CityLevelEntity::getCity).orElse(null);
    }

    public String getCompanyName(Integer companyId) {
        return Optional.ofNullable(companyId)
                .map(id -> {
                    try {
                        return employCompanyMapper.queryCompanyById(id);
                    } catch (Exception e) {
                        logger.error("***PRO*** 查询公司失败,companyId=[{}]，error:{}", id, e);
                        Cat.logError("查询公司失败 {}", e);
                        return null;
                    }
                })
                .map(EmployCompanyEntity::getSecondCategory).orElse(null);
    }

    public EmployCompanyEntity getCompany(Integer companyId) {
        return Optional.ofNullable(companyId)
                .map(id -> {
                    try {
                        return employCompanyMapper.queryCompanyById(id);
                    } catch (Exception e) {
                        logger.error("***PRO*** 查询公司失败,companyId=[{}]，error:{}", id, e);
                        Cat.logError("查询公司失败 {}", e);
                        return null;
                    }
                }).orElse(null);
    }
}

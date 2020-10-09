package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.mapper.*;
import com.autoyol.field.progress.manage.request.ImportLogReqVo;
import com.autoyol.field.progress.manage.request.city.ImportCityReqVo;
import com.autoyol.field.progress.manage.request.city.ImportOrderReqVo;
import com.autoyol.field.progress.manage.request.supplier.ImportSupplierVo;
import com.autoyol.field.progress.manage.response.ImportLogRespVo;
import com.autoyol.field.progress.manage.response.ImportLogVo;
import com.autoyol.field.progress.manage.response.store.DownLoadAddRespVo;
import com.autoyol.field.progress.manage.response.store.ImportUpdateVo;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.ImportLogUtil.buildFailLogMsg;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static java.util.stream.IntStream.range;

@Service
public class ImportService {

    @Resource
    ImportLogMapper importLogMapper;

    @Resource
    CityLevelMapper cityLevelMapper;

    @Resource
    AttendanceMapper attendanceMapper;

    @Resource
    private AppUserInfoMapper appUserInfoMapper;

    @Resource
    StoreInfoMapper storeInfoMapper;

    @Resource
    CityService cityService;

    @Resource
    SupplierAccountMapper supplierAccountMapper;

    public ImportLogRespVo queryImportLog(ImportLogReqVo logReqVo) {
        long count = importLogMapper.countByType(logReqVo.getBusinessType());
        if (count <= 0) {
            return new ImportLogRespVo();
        }

        ImportLogRespVo logRespVo = new ImportLogRespVo();
        logRespVo.setPageSize(logReqVo.getPageSize());
        logRespVo.setTotal(count);
        logRespVo.setPageNo(getPageNo(logReqVo.getPageNo(), logRespVo.getPages()));

        List<ImportLogEntity> logList = importLogMapper.queryPageLogByType(
                new ImportLogReqEntity(logReqVo.getBusinessType(), logReqVo.getStart(), logReqVo.getPageSize()));
        return buildPageRespVo(logRespVo, logList);
    }

    private ImportLogRespVo buildPageRespVo(ImportLogRespVo logRespVo, List<ImportLogEntity> logList) {
        logRespVo.setLogList(logList.stream().map(importLogEntity -> {
            ImportLogVo importLogVo = new ImportLogVo();
            BeanUtils.copyProperties(importLogEntity, importLogVo);
            importLogVo.setCreateTime(LocalDateUtil.dateToStringFormat(importLogEntity.getCreateTime(), DATE_FULL));
            return importLogVo;
        }).collect(Collectors.toList()));
        return logRespVo;
    }

    @Transactional
    public void userInfoImport(Map<Boolean, List<AppUserInfoEntity>> map) {
        List<AppUserInfoEntity> updateList = map.get(Boolean.FALSE);
        List<AppUserInfoEntity> addList = map.get(Boolean.TRUE);

        Optional.ofNullable(addList).ifPresent(list -> {
            range(NEG_ZERO, countBatch(list.size())).forEach(i -> {
                List<AppUserInfoEntity> entities = list.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND).collect(Collectors.toList());
                appUserInfoMapper.batchInsert(entities);
            });
        });

        Optional.ofNullable(updateList).ifPresent(list -> {
            range(NEG_ZERO, countBatch(list.size())).forEach(i -> {
                List<AppUserInfoEntity> entities = list.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND).collect(Collectors.toList());
                appUserInfoMapper.batchUpdate(entities);
            });
        });
    }

    @Transactional
    public void cityImport(List<ImportCityReqVo> list, String operator) {
        Optional.ofNullable(list).ifPresent(importCity -> {
            range(NEG_ZERO, countBatch(importCity.size())).forEach(i -> {
                List<CityLevelEntity> entities = importCity.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND)
                        .map(tmp -> getCityLevelEntity(operator, tmp)).collect(Collectors.toList());
                cityLevelMapper.batchUpdate(entities);

                entities.forEach(city -> {
                    cityService.addCityToCache(city.getCityId());
                });
            });
        });
    }

    @Transactional
    public void orderImport(List<ImportOrderReqVo> list, String operator) {
        Optional.ofNullable(list).ifPresent(importOrder -> {
            range(NEG_ZERO, countBatch(importOrder.size())).forEach(i -> {
                List<CityLevelEntity> entities = importOrder.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND)
                        .map(tmp -> getCityLevelEntity(operator, tmp)).collect(Collectors.toList());
                cityLevelMapper.batchUpdate(entities);

                entities.forEach(city -> {
                    cityService.addCityToCache(city.getCityId());
                });
            });
        });
    }

    @Transactional
    public void supplierImport(List<ImportSupplierVo> list, String operator){
        Optional.ofNullable(list).ifPresent(importSupplier -> {
            range(NEG_ZERO, countBatch(importSupplier.size())).forEach(i -> {
                List<SupplierAccountEntity> entities = importSupplier.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND)
                        .map(tmp -> getSupplierAccountEntity(operator, tmp)).collect(Collectors.toList());
                supplierAccountMapper.batchUpdate(entities);
            });
        });
    }

    @Transactional
    public void storeBatchAddImport(List<DownLoadAddRespVo> list, String operator) {
        Optional.ofNullable(list).ifPresent(storeBatchAdd -> {
            range(NEG_ZERO, countBatch(storeBatchAdd.size())).forEach(i -> {
                List<StoreInfoEntity> entities = storeBatchAdd.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND)
                        .map(tmp -> getStoreInfoAddEntity(operator, tmp)).filter(Objects::nonNull).collect(Collectors.toList());
                storeInfoMapper.batchInsert(entities);
            });
        });
    }

    @Transactional
    public void storeBatchUpdateImport(List<ImportUpdateVo> list, String operator) {
        Optional.ofNullable(list).ifPresent(storeBatchUpdate -> {
            range(NEG_ZERO, countBatch(storeBatchUpdate.size())).forEach(i -> {
                List<StoreInfoEntity> entities = storeBatchUpdate.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND)
                        .map(tmp -> getStoreInfoUpdateEntity(operator, tmp)).filter(Objects::nonNull).collect(Collectors.toList());
                storeInfoMapper.batchUpdate(entities);
            });
        });
    }

    @Transactional
    public void attendanceImport(List<SysDictEntity> dictEntities, List<AttendanceEntity> list) {
        Optional.ofNullable(list).ifPresent(importList -> {
            range(NEG_ZERO, countBatch(importList.size())).forEach(i -> {
                List<AttendanceEntity> entities = importList.stream().skip(i * INT_THOUSAND).limit(INT_THOUSAND)
                        .collect(Collectors.toList());

                List<AttendanceEntity> attendanceList = entities.stream()
                        .peek(entity -> entity.setMark(getMark(dictEntities, Optional.ofNullable(entity.getStatus())
                                .map(status -> Integer.parseInt(entity.getStatus()))
                                .orElse(NEG_ZERO))
                        ))
                        .collect(Collectors.toList());
                attendanceMapper.batchUpdate(attendanceList);
            });
        });
    }

    private StoreInfoEntity getStoreInfoUpdateEntity(String operator, ImportUpdateVo tmp) {
        StoreInfoEntity entity = new StoreInfoEntity();
        try {
            ConvertBeanUtil.copyProperties(entity, tmp);
        } catch (Exception e) {
            return null;
        }
        entity.setServeStatus(NEG_ZERO);
        entity.setUpdateOp(operator);
        return entity;
    }

    private StoreInfoEntity getStoreInfoAddEntity(String operator, DownLoadAddRespVo tmp) {
        StoreInfoEntity entity = new StoreInfoEntity();
        try {
            ConvertBeanUtil.copyProperties(entity, tmp);
        } catch (Exception e) {
            return null;
        }
        entity.setCityId(Integer.parseInt(tmp.getCity()));
        entity.setCreateOp(operator);
        return entity;
    }

    private CityLevelEntity getCityLevelEntity(String operator, ImportOrderReqVo tmp) {
        CityLevelEntity entity = new CityLevelEntity();
        entity.setCityId(tmp.getCityId());
        entity.setRent(tmp.getRent());
        entity.setUpdateOp(operator);
        entity.setUpdateTime(new Date());
        return entity;
    }

    private CityLevelEntity getCityLevelEntity(String operator, ImportCityReqVo tmp) {
        CityLevelEntity entity = new CityLevelEntity();
        entity.setCityId(tmp.getCityId());
        entity.setCityLevel(tmp.getCityLevel());
        entity.setUpdateOp(operator);
        entity.setUpdateTime(new Date());
        return entity;
    }

    private SupplierAccountEntity getSupplierAccountEntity(String operator, ImportSupplierVo tmp) {
        SupplierAccountEntity entity = new SupplierAccountEntity();
        entity.setUserId(tmp.getUserId());
        entity.setCompanyId(tmp.getCompanyId());
        entity.setEmail(tmp.getEmail());
        entity.setMannageCity(tmp.getMannageCity());
        entity.setIsEnable(tmp.getStatusKey());
        entity.setUpdateOp(operator);
        return entity;
    }

    public void saveImportLog(String matchSucceed, AtomicInteger matchFailCount, StringBuffer matchFail,
                              String importSucceed, AtomicInteger importFailCount, StringBuffer importFail,
                              String operator, Integer businessType) {
        ImportLogEntity logEntity = new ImportLogEntity();
        logEntity.setBusinessType(businessType);
        logEntity.setCreateOp(operator);
        logEntity.setMatchRecordSucc(matchSucceed);
        logEntity.setMatchRecordFail(buildFailLogMsg(matchFailCount, matchFail));
        logEntity.setImportRecordSucc(importSucceed);
        logEntity.setImportRecordFail(buildFailLogMsg(importFailCount, importFail));
        importLogMapper.insertSelective(logEntity);
    }
}

package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.CityLevelEntity;
import com.autoyol.field.progress.manage.entity.StoreInfoEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.StoreInfoMapper;
import com.autoyol.field.progress.manage.request.store.StoreAddReqVo;
import com.autoyol.field.progress.manage.request.store.StorePageReqVo;
import com.autoyol.field.progress.manage.request.store.StoreUpdateReqVo;
import com.autoyol.field.progress.manage.response.store.DownLoadAddRespVo;
import com.autoyol.field.progress.manage.response.store.ImportUpdateVo;
import com.autoyol.field.progress.manage.response.store.StoreInfoPageVo;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;
import com.autoyol.field.progress.manage.util.poi.PoiUtil;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.ImportLogUtil.appendLog;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.format;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.parse;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static java.util.stream.IntStream.range;

@Service
public class StoreService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    StoreInfoMapper storeInfoMapper;

    @Resource
    CityCache cityCache;

    @Resource
    DictCache dictCache;

    @Resource
    ImportService importService;

    public List<StoreInfoPageVo> findStoreByCond(StorePageReqVo reqVo) throws Exception {
        StoreInfoEntity infoEntity = new StoreInfoEntity();
        infoEntity.setServeStatus(reqVo.getServeStatusKey());
        infoEntity.setStoreFullName(reqVo.getStoreFullName());
        infoEntity.setId(Objects.nonNull(reqVo.getStoreId()) && reqVo.getStoreId() > 0 ? reqVo.getStoreId() : null);
        infoEntity.setCityId(Objects.nonNull(reqVo.getCityId()) && reqVo.getCityId() > 0 ? reqVo.getCityId() : null);

        List<StoreInfoEntity> entities = storeInfoMapper.findAllByCond(infoEntity);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.EMPTY_LIST;
        }

        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(SERVE_TYPE);
        List<SysDictEntity> cityList = dictCache.getDictByTypeNameFromCache(CITY_LEVEL_TYPE);

        return entities.stream().filter(store -> hitSearch(reqVo.getStoreFullName(), store.getStoreFullName(), String::contains))
                .sorted(Comparator.comparing(StoreInfoEntity::getCreateTime).reversed())
                .map(store -> convertEntityToPageVo(dictList, store, cityList)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public int updateById(StoreUpdateReqVo reqVo) throws Exception {
        StoreInfoEntity existEntity = new StoreInfoEntity();
        existEntity.setId(reqVo.getId());
        existEntity.setStoreName(reqVo.getStoreName());
        int count = storeInfoMapper.existCond(existEntity);
        if (count > NEG_ZERO) {
            return NEG_ONE;
        }

        existEntity.setStoreName(null);
        existEntity.setStoreFullName(reqVo.getStoreFullName());
        int countFullName = storeInfoMapper.existCond(existEntity);
        if (countFullName > NEG_ZERO) {
            return NEG_TOW;
        }

        StoreInfoEntity entity = new StoreInfoEntity();
        ConvertBeanUtil.copyProperties(entity, reqVo);
        entity.setServeStatus(reqVo.getServeStatusKey());
        entity.setUpdateOp(reqVo.getHandleUser());
        return storeInfoMapper.updateById(entity);
    }

    public int add(StoreAddReqVo reqVo) throws Exception {
        StoreInfoEntity existEntity = new StoreInfoEntity();
        existEntity.setStoreName(reqVo.getStoreName());
        int count = storeInfoMapper.existCond(existEntity);
        if (count > NEG_ZERO) {
            return NEG_ONE;
        }

        existEntity.setStoreName(null);
        existEntity.setStoreFullName(reqVo.getStoreFullName());
        int countFullName = storeInfoMapper.existCond(existEntity);
        if (countFullName > NEG_ZERO) {
            return NEG_TOW;
        }

        StoreInfoEntity entity = new StoreInfoEntity();
        ConvertBeanUtil.copyProperties(entity, reqVo);
        entity.setServeStatus(reqVo.getServeStatusKey());
        entity.setCreateOp(reqVo.getHandleUser());
        return storeInfoMapper.insertSelective(entity);
    }

    public int importBatchAdd(Workbook book, String operator) throws Exception {
        int count = NEG_ZERO;
        StringBuilder matchSucceed = new StringBuilder(CH_SUCCESS);
        StringBuffer matchFail = new StringBuffer();
        StringBuilder importSucceed = new StringBuilder(CH_SUCCESS);
        StringBuffer importFail = new StringBuffer();

        AtomicInteger matchSucceedCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger matchFailCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger importSucceedCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger importFailCount = new AtomicInteger(NEG_ZERO);

        List<DownLoadAddRespVo> downLoadAddRespVos = Lists.newArrayList();
        range(NEG_ZERO, book.getNumberOfSheets()).forEach(i -> {
            Sheet sheet = book.getSheetAt(i);
            for (int j = NEG_ZERO; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);

                if (j == NEG_ZERO) {
                    continue;
                }

                DownLoadAddRespVo vo = new DownLoadAddRespVo();
                vo.setStoreName(PoiUtil.getValue(row.getCell(0)));

                StoreInfoEntity existEntity = new StoreInfoEntity();
                existEntity.setStoreName(vo.getStoreName());
                int countName = storeInfoMapper.existCond(existEntity);
                if (countName > NEG_ZERO) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setStoreFullName(PoiUtil.getValue(row.getCell(1)));
                if (StringUtils.isBlank(vo.getStoreFullName()) || StringUtils.length(vo.getStoreFullName()) > 20) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                existEntity.setStoreName(null);
                existEntity.setStoreFullName(vo.getStoreFullName());
                int countFullName = storeInfoMapper.existCond(existEntity);
                if (countFullName > NEG_ZERO) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setBranchCompany(PoiUtil.getValue(row.getCell(2)));
                if (!StringUtils.isBlank(vo.getBranchCompany()) && StringUtils.length(vo.getBranchCompany()) > 15) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setCity(PoiUtil.getValue(row.getCell(3)));
                try {
                    int cityId = cityCache.getCityIdByCityNameFromCache(vo.getCity());
                    if (cityId < NEG_ZERO) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    vo.setCity(String.valueOf(cityId));
                } catch (Exception e) {
                    logger.error("cityCache 查询异常 {}", e);
                    Cat.logError("cityCache 查询异常 {}", e);
                    break;
                }

                vo.setDistrictCounty(PoiUtil.getValue(row.getCell(5)));
                if (StringUtils.isBlank(vo.getDistrictCounty()) || StringUtils.length(vo.getDistrictCounty()) > 10) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                vo.setStoreAddress(PoiUtil.getValue(row.getCell(6)));
                if (StringUtils.isBlank(vo.getStoreAddress()) || StringUtils.length(vo.getStoreAddress()) > 100) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                vo.setStoreContactName(PoiUtil.getValue(row.getCell(9)));
                if (!StringUtils.isBlank(vo.getStoreContactName()) && StringUtils.length(vo.getStoreContactName()) > 10) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                vo.setStoreContactMobile(PoiUtil.getValue(row.getCell(10)));
                if (StringUtils.isBlank(vo.getStoreContactMobile()) || !Pattern.matches(PHONE_REG, vo.getStoreContactMobile())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setStartTime(PoiUtil.getValue(row.getCell(11)));
                if (StringUtils.isBlank(vo.getStartTime())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                try {
                    if (vo.getStartTime().contains(CST)) {
                        Date date = parse(vo.getStartTime(), DATE_CST_FORMAT, Locale.US);
                        vo.setStartTime(format(date, DATE_TIME_NO_SECOND, Locale.CHINA));
                    }
                } catch (Exception e) {
                    logger.error("***PRO*** 开始时间转换错误 {}", e);
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setEndTime(PoiUtil.getValue(row.getCell(12)));
                if (StringUtils.isBlank(vo.getEndTime())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                try {
                    if (vo.getEndTime().contains(CST)) {
                        Date date = parse(vo.getEndTime(), DATE_CST_FORMAT, Locale.US);
                        vo.setEndTime(format(date, DATE_TIME_NO_SECOND, Locale.CHINA));
                    }
                } catch (Exception e) {
                    logger.error("***PRO*** 结束时间转换错误 {}", e);
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                importSucceedCount.incrementAndGet();
                downLoadAddRespVos.add(vo);
            }
        });
        // 有相同的门店去重
        List<DownLoadAddRespVo> distinctList = downLoadAddRespVos.stream().distinct().collect(Collectors.toList());
        // 空白Excel直接结束
        if (isAllLessThanZero(matchFailCount, matchSucceedCount, importFailCount, importSucceedCount)) {
            return NEG_ONE;
        }

        AtomicInteger mobileCount = getMultiCount(distinctList.stream().map(DownLoadAddRespVo::getStoreName));
        if (mobileCount.get() > NEG_ZERO) {
            return NEG_FIVE;
        }

        AtomicInteger idNoCount = getMultiCount(distinctList.stream().map(DownLoadAddRespVo::getStoreFullName));
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
                importSucceed.toString(), importFailCount, importFail, operator,
                Integer.parseInt(CommonEnum.LOG_TYPE_STORE_BATCH_ADD.getCode()));

        if (CollectionUtils.isEmpty(distinctList)) {
            return count;
        }

        logger.info("***PRO***准备保存门店批量新增导入数据 {}", distinctList.size());
        importService.storeBatchAddImport(distinctList, operator);
        return count;
    }

    public int importBatchUpdate(Workbook book, String operator) throws Exception {
        int count = NEG_ZERO;
        StringBuilder matchSucceed = new StringBuilder(CH_SUCCESS);
        StringBuffer matchFail = new StringBuffer();
        StringBuilder importSucceed = new StringBuilder(CH_SUCCESS);
        StringBuffer importFail = new StringBuffer();

        AtomicInteger matchSucceedCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger matchFailCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger importSucceedCount = new AtomicInteger(NEG_ZERO);
        AtomicInteger importFailCount = new AtomicInteger(NEG_ZERO);

        List<ImportUpdateVo> updateVos = Lists.newArrayList();
        range(NEG_ZERO, book.getNumberOfSheets()).forEach(i -> {
            Sheet sheet = book.getSheetAt(i);
            for (int j = NEG_ZERO; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);

                if (j == NEG_ZERO) {
                    continue;
                }

                ImportUpdateVo vo = new ImportUpdateVo();

                try {
                    vo.setId(Integer.parseInt(PoiUtil.getValue(row.getCell(0))));
                } catch (NumberFormatException e) {
                    appendLog(matchFail, matchFailCount, j);
                    continue;
                }

                vo.setStoreName(PoiUtil.getValue(row.getCell(1)));
                StoreInfoEntity existEntity = new StoreInfoEntity();
                existEntity.setId(vo.getId());
                existEntity.setStoreName(vo.getStoreName());
                int countName = storeInfoMapper.existCond(existEntity);
                if (countName > NEG_ZERO) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                vo.setStoreFullName(PoiUtil.getValue(row.getCell(2)));
                if (StringUtils.isBlank(vo.getStoreFullName()) || StringUtils.length(vo.getStoreFullName()) > 20) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                existEntity.setStoreName(null);
                existEntity.setStoreFullName(vo.getStoreFullName());
                int countFullName = storeInfoMapper.existCond(existEntity);
                if (countFullName > NEG_ZERO) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setBranchCompany(PoiUtil.getValue(row.getCell(3)));
                if (!StringUtils.isBlank(vo.getBranchCompany()) && StringUtils.length(vo.getBranchCompany()) > 15) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setDistrictCounty(PoiUtil.getValue(row.getCell(6)));
                if (StringUtils.isBlank(vo.getDistrictCounty()) || StringUtils.length(vo.getDistrictCounty()) > 10) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                vo.setStoreAddress(PoiUtil.getValue(row.getCell(7)));
                if (StringUtils.isBlank(vo.getStoreAddress()) || StringUtils.length(vo.getStoreAddress()) > 100) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                vo.setStoreContactName(PoiUtil.getValue(row.getCell(10)));
                if (!StringUtils.isBlank(vo.getStoreContactName()) && StringUtils.length(vo.getStoreContactName()) > 10) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                vo.setStoreContactMobile(PoiUtil.getValue(row.getCell(11)));
                if (StringUtils.isBlank(vo.getStoreContactMobile()) || !Pattern.matches(PHONE_REG, vo.getStoreContactMobile())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setStartTime(PoiUtil.getValue(row.getCell(12)));
                if (StringUtils.isBlank(vo.getStartTime())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                try {
                    if (vo.getStartTime().contains(CST)) {
                        Date date = parse(vo.getStartTime(), DATE_CST_FORMAT, Locale.US);
                        vo.setStartTime(format(date, DATE_TIME, Locale.CHINA));
                    }
                } catch (Exception e) {
                    logger.error("***PRO*** 开始时间转换错误 {}", e);
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                vo.setEndTime(PoiUtil.getValue(row.getCell(13)));
                if (StringUtils.isBlank(vo.getEndTime())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                try {
                    if (vo.getEndTime().contains(CST)) {
                        Date date = parse(vo.getEndTime(), DATE_CST_FORMAT, Locale.US);
                        vo.setEndTime(format(date, DATE_TIME, Locale.CHINA));
                    }
                } catch (Exception e) {
                    logger.error("***PRO*** 结束时间转换错误 {}", e);
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                importSucceedCount.incrementAndGet();
                updateVos.add(vo);
            }
        });
        // 有相同的门店去重
        List<ImportUpdateVo> distinctList = updateVos.stream().distinct().collect(Collectors.toList());
        // 空白Excel直接结束
        boolean isBlank = matchFailCount.get() <= 0 && matchSucceedCount.get() <= 0 && importFailCount.get() <= 0 && importSucceedCount.get() <= 0;
        if (isBlank) {
            return NEG_ONE;
        }

        AtomicInteger mobileCount = getMultiCount(distinctList.stream().map(ImportUpdateVo::getStoreName));
        if (mobileCount.get() > NEG_ZERO) {
            return NEG_FIVE;
        }

        AtomicInteger idNoCount = getMultiCount(distinctList.stream().map(ImportUpdateVo::getStoreFullName));
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
                importSucceed.toString(), importFailCount, importFail, operator,
                Integer.parseInt(CommonEnum.LOG_TYPE_STORE_BATCH_UPDATE.getCode()));

        if (CollectionUtils.isEmpty(distinctList)) {
            return count;
        }

        logger.info("***PRO***准备保存门店批量新增导入数据 {}", distinctList.size());
        importService.storeBatchUpdateImport(distinctList, operator);
        return count;
    }

    private StoreInfoPageVo convertEntityToPageVo(List<SysDictEntity> dictList, StoreInfoEntity store, List<SysDictEntity> cityList) {
        try {
            StoreInfoPageVo pageVo = new StoreInfoPageVo();
            ConvertBeanUtil.copyProperties(pageVo, store);
            CityLevelEntity cityEntity = cityCache.getCityByIdFromCache(store.getCityId());
            Optional.ofNullable(cityEntity).ifPresent(city -> {
                pageVo.setCityId(city.getCityId());
                pageVo.setCity(city.getCity());
                pageVo.setCityLevel(cityList.stream().filter(dict -> dict.getCode().equals(city.getCityLevel())).findFirst()
                        .map(SysDictEntity::getLabel).orElse(STRING_EMPTY));
            });
            pageVo.setServeStatusKey(store.getServeStatus());
            Optional.ofNullable(dictList)
                    .ifPresent(dictLst -> pageVo.setServeStatusVal(dictLst.stream()
                            .filter(dict -> dict.getCode().equals(store.getServeStatus()))
                            .map(SysDictEntity::getLabel).findFirst()
                            .orElse(STRING_EMPTY)));
            return pageVo;
        } catch (Exception e) {
            logger.error("***PRO*** 查询失败 {}", e);
            return null;
        }
    }
}

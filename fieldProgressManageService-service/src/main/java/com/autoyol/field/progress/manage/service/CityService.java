package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.CityLevelEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.CityLevelMapper;
import com.autoyol.field.progress.manage.request.city.CityLevelUpdateReqVo;
import com.autoyol.field.progress.manage.request.city.ImportCityReqVo;
import com.autoyol.field.progress.manage.response.city.*;
import com.autoyol.field.progress.manage.util.OprUtil;
import com.autoyol.field.progress.manage.util.poi.PoiUtil;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.ImportLogUtil.appendLog;
import static com.autoyol.field.progress.manage.util.OprUtil.isAllLessThanZero;
import static java.util.stream.IntStream.range;

@Service
public class CityService extends AbstractService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CityCache cityCache;

    @Resource
    DictCache dictCache;

    @Resource
    ImportService importService;

    @Resource
    private CityLevelMapper cityLevelMapper;

    public List<CityLevelVo> findCityByCond(final Integer cityId) throws Exception {
        List<CityLevelEntity> cityList = cityCache.getAllCityFromCache();
        if (CollectionUtils.isEmpty(cityList)) {
            return Collections.EMPTY_LIST;
        }
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(CITY_LEVEL_TYPE);
        return cityList.stream()
                .filter(entity -> OprUtil.hitSearch(cityId, entity.getCityId(), Integer::equals))
                .sorted(Comparator.comparing(CityLevelEntity::getCityId))
                .map(entity -> {
                    CityLevelVo respVo = new CityLevelVo();
                    BeanUtils.copyProperties(entity, respVo);
                    respVo.setCityLevelKey(entity.getCityLevel());
                    respVo.setCityLevelVal(dictList.stream().filter(dict -> dict.getCode().equals(entity.getCityLevel())).findFirst()
                            .map(SysDictEntity::getLabel).orElse(STRING_EMPTY));
                    return respVo;
                }).collect(Collectors.toList());
    }

    public CityLevelQueryRespVo selectCityById(Integer cityId) throws Exception {
        CityLevelEntity entity = cityCache.getCityByIdFromCache(cityId);
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(CITY_LEVEL_TYPE);
        CityLevelQueryRespVo cityLevelQueryRespVo = new CityLevelQueryRespVo();
        BeanUtils.copyProperties(entity, cityLevelQueryRespVo);
        cityLevelQueryRespVo.setCityLevelKey(entity.getCityLevel());
        cityLevelQueryRespVo.setCityLevelVal(dictList.stream().filter(dict -> dict.getCode().equals(entity.getCityLevel())).findFirst()
                .map(SysDictEntity::getLabel).orElse(STRING_EMPTY));
        return cityLevelQueryRespVo;
    }

    public Integer updateCityById(CityLevelUpdateReqVo reqVo) {
        CityLevelEntity entity = new CityLevelEntity();
        BeanUtils.copyProperties(reqVo, entity);
        entity.setCityLevel(reqVo.getCityLevelKey());
        entity.setUpdateOp(reqVo.getHandleUser());
        Integer count = cityLevelMapper.updateCityById(entity);
        if (Objects.nonNull(count) && count == INT_ONE) {
            deleteRedisCityCache(String.valueOf(reqVo.getCityId()));
            addCityToCache(reqVo.getCityId());
        }
        return Optional.ofNullable(count).orElse(NEG_ONE);
    }

    protected void addCityToCache(Integer cityId) {
        try {
            CityLevelEntity city = cityLevelMapper.selectByPrimaryKey(cityId);
            cityCache.addCityToCache(city);
        } catch (Exception e) {
            logger.error("***RESP*** 增加城市 redis异常！ e:{}", e);
            Cat.logError("增加城市 redis异常 {}", e);
        }
    }

    public void deleteRedisCityCache(Object... cityIdList) {
        try {
            cityCache.deleteCityCache(cityIdList);
        } catch (Exception e) {
            logger.error("***RESP*** 刪除城市 redis异常！ e:{}", e);
            Cat.logError("刪除城市 redis异常 {}", e);
        }
    }

    public CityLevelAllRespVo findAllCity() throws Exception {
        List<CityLevelEntity> cityList = cityCache.getAllCityFromCache();
        if (CollectionUtils.isEmpty(cityList)) {
            return new CityLevelAllRespVo();
        }
        List<CityLevelEntity> sortedCity = cityList.stream().sorted(Comparator.comparing(CityLevelEntity::getCityId)).collect(Collectors.toList());
        CityLevelAllRespVo allRespVo = new CityLevelAllRespVo();
        allRespVo.setNavBarVos(getNavBarVos(sortedCity));
        allRespVo.setPrefixVos(getPrefixVos(sortedCity));
        return allRespVo;
    }

    @Override
    public int importObj(Workbook book, String operator) throws Exception {
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(CITY_LEVEL_TYPE);
        List<ImportCityReqVo> importCityList = Lists.newArrayList();
        range(NEG_ZERO, book.getNumberOfSheets()).forEach(i -> {
            Sheet sheet = book.getSheetAt(i);
            for (int j = NEG_ZERO; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);

                if (j == NEG_ZERO) {
                    continue;
                }

                ImportCityReqVo importCityReqVo = new ImportCityReqVo();
                importCityReqVo.setCity(PoiUtil.getValue(row.getCell(0)));
                if (StringUtils.isBlank(importCityReqVo.getCity())) {
                    appendLog(matchFail, matchFailCount, j);
                    continue;
                }
                try {
                    int cityId = cityCache.getCityIdByCityNameFromCache(importCityReqVo.getCity());
                    if (cityId < NEG_ZERO) {
                        appendLog(matchFail, matchFailCount, j);
                        continue;
                    }
                    importCityReqVo.setCityId(cityId);
                } catch (Exception e) {
                    logger.error("cityCache 查询异常 {}", e);
                    Cat.logError("cityCache 查询异常 {}", e);
                    break;
                }
                matchSucceedCount.incrementAndGet();

                String levelStr = PoiUtil.getValue(row.getCell(1));
                SysDictEntity dictEntity = dictList.stream().filter(dict -> OprUtil.objEquals(levelStr, dict.getLabel(), String::equals))
                        .findFirst().orElse(null);
                if (Objects.isNull(dictEntity)) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                importCityReqVo.setCityLevel(dictEntity.getCode());

                importSucceedCount.incrementAndGet();
                importCityList.add(importCityReqVo);
            }
        });
        // 有相同的城市不同的等級，直接结束
        List<ImportCityReqVo> distinctList = importCityList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctList) && isAllLessThanZero(matchFailCount, matchSucceedCount, importFailCount, importSucceedCount)) {
            return NEG_ONE;
        }

        if (distinctField(distinctList).size() < distinctList.size()) {
            return NEG_TOW;
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
                Integer.parseInt(CommonEnum.LOG_TYPE_CITY_LEVEL.getCode()));

        if (CollectionUtils.isEmpty(distinctList)) {
            return count;
        }

        deleteRedisCityCache((Object) distinctList.stream().map(ImportCityReqVo::getCityId).map(String::valueOf).toArray(String[]::new));
        logger.info("***PRO***准备保存城市导入数据 {}", distinctList.size());
        importService.cityImport(distinctList, operator);
        return count;
    }

    private ArrayList<ImportCityReqVo> distinctField(List<ImportCityReqVo> distinctList) {
        return distinctList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(ImportCityReqVo::getCity))), ArrayList::new));
    }

    public List<CityLevelNavBarVo> getNavBarVos(List<CityLevelEntity> cityList) throws Exception {
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(CITY_LEVEL_TYPE);
        return cityList.stream().map(entity -> {
            CityLevelNavBarVo respVo = new CityLevelNavBarVo();
            BeanUtils.copyProperties(entity, respVo);
            respVo.setCityLevelKey(entity.getCityLevel());
            respVo.setCityLevelVal(dictList.stream().filter(dict -> dict.getCode().equals(entity.getCityLevel())).findFirst()
                    .map(SysDictEntity::getLabel).orElse(STRING_EMPTY));
            return respVo;
        }).collect(Collectors.toList());
    }

    public List<CityPrefixVo> getPrefixVos(List<CityLevelEntity> cityList) {
        return cityList.stream().filter(entity -> StringUtils.isNotBlank(entity.getFullLetter()))
                .map(entity -> entity.getFullLetter().substring(0, 1))
                .distinct().map(CityPrefixVo::new).collect(Collectors.toList());
    }

}

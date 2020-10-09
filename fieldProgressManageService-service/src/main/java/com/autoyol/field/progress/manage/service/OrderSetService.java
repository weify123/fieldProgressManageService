package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.entity.CityLevelEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.CityLevelMapper;
import com.autoyol.field.progress.manage.request.city.ImportOrderReqVo;
import com.autoyol.field.progress.manage.request.city.OrderUpdateReqVo;
import com.autoyol.field.progress.manage.response.city.OrderSetVo;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.ImportLogUtil.appendLog;
import static com.autoyol.field.progress.manage.util.OprUtil.getObjOrDefault;
import static com.autoyol.field.progress.manage.util.OprUtil.isAllLessThanZero;
import static java.util.stream.IntStream.range;

@Service
public class OrderSetService extends AbstractService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CityCache cityCache;

    @Resource
    private CityLevelMapper cityLevelMapper;

    @Resource
    CityService cityService;

    @Resource
    ImportService importService;

    public List<OrderSetVo> findOrderByCond(final Integer cityId) throws Exception {
        List<CityLevelEntity> cityList = cityCache.getAllCityFromCache();
        if (CollectionUtils.isEmpty(cityList)) {
            return Collections.EMPTY_LIST;
        }
        return cityList.stream()
                .filter(entity -> OprUtil.hitSearch(cityId, entity.getCityId(), Integer::equals))
                .filter(entity -> Objects.nonNull(entity.getRent()))
                .sorted(Comparator.comparing(CityLevelEntity::getCityId))
                .map(entity -> {
                    OrderSetVo respVo = new OrderSetVo();
                    BeanUtils.copyProperties(entity, respVo);
                    respVo.setRent(Optional.ofNullable(entity.getRent())
                            .map(r -> r.setScale(NEG_ZERO, BigDecimal.ROUND_DOWN))
                            .map(BigDecimal::toPlainString).orElse(STRING_EMPTY));
                    return respVo;
                }).collect(Collectors.toList());
    }

    public OrderSetVo selectOrderByCityId(Integer cityId) throws Exception {
        CityLevelEntity entity = cityCache.getCityByIdFromCache(cityId);
        OrderSetVo orderSetVo = new OrderSetVo();
        if (Objects.isNull(entity) || Objects.isNull(entity.getRent())) {
            return null;
        }
        BeanUtils.copyProperties(entity, orderSetVo);
        orderSetVo.setRent(Optional.ofNullable(entity.getRent())
                .map(r -> r.setScale(NEG_ZERO, BigDecimal.ROUND_DOWN))
                .map(BigDecimal::toPlainString).orElse(STRING_EMPTY));
        return orderSetVo;

    }

    public int addOrUpdateOrder(OrderUpdateReqVo reqVo) {
        if (reqVo.getIsAdd()) {
            CityLevelEntity entity = cityLevelMapper.selectByPrimaryKey(reqVo.getCityId());
            if (Objects.nonNull(entity) && Objects.nonNull(entity.getRent())) {
                return NEG_ONE;
            }
        }
        CityLevelEntity entity = new CityLevelEntity();
        BeanUtils.copyProperties(reqVo, entity);
        entity.setRent(new BigDecimal(reqVo.getRent()));
        entity.setUpdateOp(reqVo.getHandleUser());
        Integer count = cityLevelMapper.updateCityById(entity);
        if (Objects.nonNull(count) && count == INT_ONE) {
            cityService.deleteRedisCityCache(String.valueOf(reqVo.getCityId()));
            cityService.addCityToCache(reqVo.getCityId());
        }
        return getObjOrDefault(count, Function.identity(), NEG_ONE);
    }

    @Override
    public int importObj(Workbook book, String operator) {
        List<ImportOrderReqVo> importCityList = Lists.newArrayList();
        range(NEG_ZERO, book.getNumberOfSheets()).forEach(i -> {
            Sheet sheet = book.getSheetAt(i);
            for (int j = NEG_ZERO; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);

                if (j == NEG_ZERO) {
                    continue;
                }

                ImportOrderReqVo importOrderReqVo = new ImportOrderReqVo();
                importOrderReqVo.setCity(PoiUtil.getValue(row.getCell(0)));
                if (StringUtils.isBlank(importOrderReqVo.getCity())) {
                    appendLog(matchFail, matchFailCount, j);
                    continue;
                }
                try {
                    int cityId = cityCache.getCityIdByCityNameFromCache(importOrderReqVo.getCity());
                    if (cityId < NEG_ZERO) {
                        appendLog(matchFail, matchFailCount, j);
                        continue;
                    }
                    importOrderReqVo.setCityId(cityId);
                } catch (Exception e) {
                    logger.error("cityCache 查询异常 {}", e);
                    Cat.logError("cityCache 查询异常 {}", e);
                    break;
                }
                matchSucceedCount.incrementAndGet();

                try {
                    importOrderReqVo.setRent(new BigDecimal(PoiUtil.getValue(row.getCell(1))));
                } catch (Exception e) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                importSucceedCount.incrementAndGet();
                importCityList.add(importOrderReqVo);
            }
        });

        // 空白Excel直接结束
        List<ImportOrderReqVo> distinctList = importCityList.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctList) && isAllLessThanZero(matchFailCount, matchSucceedCount, importFailCount, importSucceedCount)) {
            return NEG_ONE;
        }
        // 有相同的订单设定不同的租金，直接结束
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
                Integer.parseInt(CommonEnum.LOG_TYPE_ORDER_SET.getCode()));

        if (CollectionUtils.isEmpty(distinctList)) {
            return count;
        }

        cityService.deleteRedisCityCache((Object) distinctList.stream().map(ImportOrderReqVo::getCityId).map(String::valueOf).toArray(String[]::new));
        logger.info("***PRO***准备保存订单设定导入数据 {}", distinctList.size());
        importService.orderImport(distinctList, operator);
        return count;
    }

    private ArrayList<ImportOrderReqVo> distinctField(List<ImportOrderReqVo> distinctList) {
        return distinctList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(ImportOrderReqVo::getCity))), ArrayList::new));
    }
}

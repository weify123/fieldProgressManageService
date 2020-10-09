package com.autoyol.field.progress.manage.service;

import com.alibaba.fastjson.JSON;
import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.mapper.SupplierAccountMapper;
import com.autoyol.field.progress.manage.mapper.TransOrderInfoMapper;
import com.autoyol.field.progress.manage.request.supplier.ImportSupplierVo;
import com.autoyol.field.progress.manage.request.supplier.SupplierPageReqVo;
import com.autoyol.field.progress.manage.request.supplier.SupplierUpdateVo;
import com.autoyol.field.progress.manage.response.company.CompanyCategoryRespVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierCompanyRespVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierCompanyVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierRoleVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierVo;
import com.autoyol.field.progress.manage.util.poi.PoiUtil;
import com.autoyol.sop.util.mq.UserCreateMQ;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.ImportLogUtil.appendLog;
import static com.autoyol.field.progress.manage.util.OprUtil.*;
import static java.util.stream.IntStream.range;

@Service
public class SupplierAccountService extends AbstractService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    SupplierAccountMapper supplierAccountMapper;

    @Resource
    TransOrderInfoMapper transOrderInfoMapper;

    @Resource
    CityCache cityCache;

    @Resource
    DictCache dictCache;

    @Resource
    ImportService importService;

    @Resource
    CityService cityService;

    @Resource
    CompanyService companyService;

    @Resource
    EmployCompanyMapper employCompanyMapper;

    @Resource
    MsgService msgService;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    @Value("${supplier.company.first}")
    private String supplierCompanyFirst;

    private void syncSupplier(SupplierAccountEntity record, String companyName, String operator) {
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            Map<String, Object> map = msgService.buildMap();
            map.put("id", record.getId());
            map.put("userName", record.getUserName());
            map.put("companyName", companyName);
            map.put("email", record.getEmail());
            map.put("manageCity", record.getMannageCity());
            map.put("isDelete", record.getIsDelete());
            map.put("isEnable", getObjOrDefault(record.getIsEnable(), Function.identity(), NEG_ZERO));
            map.put("operator", operator);
            msgService.sendMq(sqlServer_supplier_mq_exchange, sqlServer_supplier_mq_routing_key, INT_SIX, map);
        }
    }

    /**
     * 企业版同步
     *
     * @param user
     */
    public void saveOrUpdate(UserCreateMQ user) {
        SupplierAccountEntity accountEntity = supplierAccountMapper.selectSupplierByUserId(String.valueOf(user.getId()));
        SupplierAccountEntity entity = new SupplierAccountEntity();
        entity.setUserId(getObjOrDefault(user.getId(), String::valueOf, null));
        entity.setUserName(user.getName());
        entity.setEmail(user.getEmail());
        if (Objects.isNull(user.getAdmin())) {
            entity.setIsDelete(INT_ONE);
        }

        EmployCompanyEntity companyEntity;
        if (Objects.isNull(accountEntity)) {
            companyEntity = employCompanyMapper.queryCompanyByTenantId(user.getTenantId());
            entity.setCompanyId(companyEntity.getId());
            supplierAccountMapper.insertSelective(entity);
        } else {
            companyEntity = employCompanyMapper.queryCompanyById(accountEntity.getCompanyId());
            entity.setCompanyId(companyEntity.getId());
            entity.setUserId(accountEntity.getUserId());
            supplierAccountMapper.updateByUserId(entity);
        }
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {

            syncSupplier(entity, companyEntity.getSecondCategory(), null);
        }
    }

    /**
     * 仁云同步field
     * @param map
     */
    public void saveOrUpdate(Map map) throws Exception {
        SupplierAccountEntity accountEntity = JSON.parseObject(JSON.toJSONString(map), SupplierAccountEntity.class);
        SupplierAccountEntity account = supplierAccountMapper.selectSupplierByName(accountEntity.getUserName());
        if (Objects.isNull(account)){// 有待确认
            return;
        }
        EmployCompanyEntity companyEntity = employCompanyMapper.getObjByName((String) map.get("companyName"));
        if (Objects.isNull(companyEntity)){
            return;
        }
        accountEntity.setCompanyId(companyEntity.getId());
        accountEntity.setMannageCity(getCityIdStr(accountEntity.getMannageCity()));
        accountEntity.setUpdateOp((String) map.get("operator"));
        supplierAccountMapper.updateById(accountEntity);
    }

    public SupplierAccountEntity selectByUserId(String userId) {
        return supplierAccountMapper.selectSupplierByUserId(userId);
    }

    public List<SupplierVo> findSupplierAccountByPage(SupplierPageReqVo reqVo) throws Exception {
        SupplierAccountEntity supplierAccountEntity = new SupplierAccountEntity();
        supplierAccountEntity.setCompanyId(reqVo.getCompanyId());
        supplierAccountEntity.setIsEnable(reqVo.getStatusKey());
        List<SupplierAccountEntity> supplierAccountEntities = supplierAccountMapper.queryCond(supplierAccountEntity);
        if (CollectionUtils.isEmpty(supplierAccountEntities)) {
            return Collections.EMPTY_LIST;
        }

        List<SysDictEntity> enableDictList = dictCache.getDictByTypeNameFromCache(USER_STATUS);
        return supplierAccountEntities.stream()
                .filter(supplier -> hitSearch(reqVo.getUserName(), supplier.getUserName(), String::contains))
                .filter(supplier -> matchListSearch(getList(supplier.getMannageCity(), SPLIT_DAWN, Integer::parseInt),
                        reqVo.getCityId(), List::contains))
                .map(supplier -> {
                    SupplierVo supplierVo = setSupplierVo(supplier);
                    supplierVo.setManageCityIdStr(supplier.getMannageCity());
                    setCompanyAndCity(supplier, supplierVo);
                    supplierVo.setStatusVal(getLabelByCode(enableDictList, String.valueOf(supplier.getIsEnable())));
                    return supplierVo;
                }).collect(Collectors.toList());
    }

    public SupplierVo selectById(Integer id) throws Exception {
        SupplierAccountEntity accountEntity = supplierAccountMapper.selectByPrimaryKey(id);
        if (Objects.isNull(accountEntity)) {
            return new SupplierVo();
        }

        List<SysDictEntity> enableDictList = dictCache.getDictByTypeNameFromCache(USER_STATUS);
        SupplierVo supplierVo = setSupplierVo(accountEntity);
        supplierVo.setManageCityIdStr(accountEntity.getMannageCity());
        setCompanyAndCity(accountEntity, supplierVo);
        supplierVo.setStatusVal(getLabelByCode(enableDictList, String.valueOf(accountEntity.getIsEnable())));
        return supplierVo;
    }

    public CompanyCategoryRespVo queryCompany() throws Exception {
        return companyService.queryCompanyCategory(supplierCompanyFirst);
    }

    public SupplierCompanyRespVo querySupplierCompany(String transOrderNo) throws Exception {
        SupplierCompanyRespVo respVo = new SupplierCompanyRespVo();
        TransOrderInfoEntity orderInfoEntity = transOrderInfoMapper.queryByOrderNo(transOrderNo);
        if (Objects.isNull(orderInfoEntity)) {
            return respVo;
        }
        Integer cityId = cityCache.getCityIdByCityNameFromCache(orderInfoEntity.getBelongCity());
        if (Objects.isNull(cityId)) {
            return respVo;
        }

        SupplierAccountEntity accountEntity = new SupplierAccountEntity();
        accountEntity.setIsEnable(NEG_ZERO);
        List<SupplierAccountEntity> accountEntities = supplierAccountMapper.queryCond(accountEntity);
        if (CollectionUtils.isEmpty(accountEntities)) {
            return respVo;
        }
        List<SupplierCompanyVo> list = buildCompanyByCond(cityId, accountEntities);
        respVo.setList(list);
        return respVo;
    }

    private List<SupplierCompanyVo> buildCompanyByCond(Integer cityId, List<SupplierAccountEntity> accountEntities) {
        return accountEntities.stream()
                .filter(account -> !StringUtils.isEmpty(account.getMannageCity()))
                .filter(account -> getList(account.getMannageCity(), SPLIT_DAWN, Integer::parseInt).contains(cityId))
                .map(account -> {
                    SupplierCompanyVo companyVo = new SupplierCompanyVo();
                    companyVo.setCompanyId(account.getCompanyId());
                    EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(account.getCompanyId());
                    companyVo.setSecondCategory(getObjOrDefault(companyEntity, EmployCompanyEntity::getSecondCategory, STRING_EMPTY));
                    return companyVo;
                }).distinct().collect(Collectors.toList());
    }

    public SupplierRoleVo querySupplierByName(String userName) throws Exception {
        SupplierAccountEntity accountEntity = supplierAccountMapper.selectSupplierByName(StringUtils.trim(userName));
        SupplierRoleVo roleVo = new SupplierRoleVo();
        if (Objects.isNull(accountEntity) || Objects.isNull(accountEntity.getCompanyId()) || accountEntity.getIsEnable().equals(INT_ONE)) {
            roleVo.setRoleId(NEG_ONE);
            return roleVo;
        }
        roleVo.setRoleId(INT_ONE);

        EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(accountEntity.getCompanyId());
        roleVo.setCompanyId(accountEntity.getCompanyId());
        roleVo.setFirstCategory(getObjOrDefault(companyEntity, EmployCompanyEntity::getFirstCategory, STRING_EMPTY));
        roleVo.setSecondCategory(getObjOrDefault(companyEntity, EmployCompanyEntity::getSecondCategory, STRING_EMPTY));

        if (!StringUtils.isEmpty(accountEntity.getMannageCity())) {
            List<CityLevelEntity> cityEntityList = cityCache.getCityByIdListFromCache(
                    getList(accountEntity.getMannageCity(), SPLIT_DAWN, Integer::parseInt));

            List<CityLevelEntity> sortedCity = cityEntityList.stream().sorted(Comparator.comparing(CityLevelEntity::getCityId)).collect(Collectors.toList());
            roleVo.setNavBarVos(cityService.getNavBarVos(sortedCity));
            roleVo.setPrefixVos(cityService.getPrefixVos(sortedCity));
        }
        return roleVo;
    }

    public int updateById(SupplierUpdateVo reqVo) {
        SupplierAccountEntity accountEntity = new SupplierAccountEntity();
        BeanUtils.copyProperties(reqVo, accountEntity);
        accountEntity.setMannageCity(
                getList(reqVo.getManageCityIdStr(), SPLIT_DAWN, s -> s).stream().distinct().reduce((x, y) -> x + SPLIT_DAWN + y).orElse(null));
        accountEntity.setIsEnable(reqVo.getStatusKey());
        accountEntity.setUpdateOp(reqVo.getHandleUser());
        int count = supplierAccountMapper.updateById(accountEntity);

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            SupplierAccountEntity account = supplierAccountMapper.selectByPrimaryKey(reqVo.getId());
            EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(account.getCompanyId());
            accountEntity.setMannageCity(reqVo.getManageCityIdStr());
            syncSupplier(accountEntity, companyEntity.getSecondCategory(), reqVo.getHandleUser());
        }
        return count;
    }

    private void setCompanyAndCity(SupplierAccountEntity supplier, SupplierVo supplierVo) {
        try {
            if (Objects.nonNull(supplier.getCompanyId())) {
                EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(supplier.getCompanyId());
                supplierVo.setFirstCategory(Optional.ofNullable(companyEntity).map(EmployCompanyEntity::getFirstCategory).orElse(STRING_EMPTY));
                supplierVo.setSecondCategory(Optional.ofNullable(companyEntity).map(EmployCompanyEntity::getSecondCategory).orElse(STRING_EMPTY));
            }
        } catch (Exception e) {
            logger.error("***PRO*** 公司查询,companyId={}，error:{}", supplier.getCompanyId(), e);
            Cat.logError("公司查询失败{}", e);
        }

        try {
            if (!StringUtils.isEmpty(supplier.getMannageCity())) {
                List<CityLevelEntity> cityEntityList = cityCache.getCityByIdListFromCache(
                        getList(supplier.getMannageCity(), SPLIT_DAWN, Integer::parseInt));
                supplierVo.setMannageCity(Optional.ofNullable(cityEntityList).filter(cityList -> !CollectionUtils.isEmpty(cityList))
                        .map(cityList -> cityList.stream().map(CityLevelEntity::getCity).reduce((x, y) -> x + SPLIT_DAWN + y).orElse(STRING_EMPTY))
                        .orElse(STRING_EMPTY));
            }

        } catch (Exception e) {
            logger.error("***PRO*** 城市查询,companyId={}，error:{}", supplier.getMannageCity(), e);
            Cat.logError("城市查询失败{}", e);
        }
    }

    private SupplierVo setSupplierVo(SupplierAccountEntity supplier) {
        SupplierVo supplierVo = new SupplierVo();
        supplierVo.setId(supplier.getId());
        supplierVo.setUserName(supplier.getUserName());
        supplierVo.setCompanyId(supplier.getCompanyId());
        supplierVo.setEmail(supplier.getEmail());
        supplierVo.setManageCityIdStr(supplier.getMannageCity());
        supplierVo.setStatusKey(supplier.getIsEnable());
        return supplierVo;
    }

    @Override
    public int importObj(Workbook book, String operator) {
        List<ImportSupplierVo> supplierVos = Lists.newArrayList();
        range(NEG_ZERO, book.getNumberOfSheets()).forEach(i -> {
            Sheet sheet = book.getSheetAt(i);
            for (int j = NEG_ZERO; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);

                if (j == NEG_ZERO) {
                    continue;
                }

                ImportSupplierVo importSupplierVo = new ImportSupplierVo();
                importSupplierVo.setUserName(PoiUtil.getValue(row.getCell(0)));
                if (StringUtils.isEmpty(importSupplierVo.getUserName())) {
                    appendLog(matchFail, matchFailCount, j);
                    continue;
                }
                String userId = supplierAccountMapper.getUserIdByUserName(importSupplierVo.getUserName());
                if (Objects.isNull(userId) || Integer.parseInt(userId) <= NEG_ZERO) {
                    appendLog(matchFail, matchFailCount, j);
                    continue;
                }
                importSupplierVo.setUserId(userId);
                matchSucceedCount.incrementAndGet();

                // 公司
                /*importSupplierVo.setCompanyName(PoiUtil.getValue(row.getCell(1)));
                if (StringUtils.isEmpty(importSupplierVo.getCompanyName())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                try {
                    EmployCompanyEntity companyEntity = companyCache.getObjByCompanyNameFromCache(importSupplierVo.getCompanyName());
                    if (Objects.isNull(companyEntity) || Objects.isNull(companyEntity.getTenantId())) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    importSupplierVo.setCompanyId(companyEntity.getTenantId());
                } catch (Exception e) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }*/
                // 邮箱
               /* importSupplierVo.setEmail(PoiUtil.getValue(row.getCell(2)));
                if (StringUtils.isEmpty(importSupplierVo.getEmail()) || !Pattern.matches(MAIL_REG, importSupplierVo.getEmail()) ||
                        importSupplierVo.getEmail().length() > 100) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }*/

                // 城市
                importSupplierVo.setMannageCity(PoiUtil.getValue(row.getCell(3)));
                if (StringUtils.isEmpty(importSupplierVo.getMannageCity())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                try {
                    String cityIdStr = getCityIdStr(importSupplierVo.getMannageCity());
                    if (StringUtils.isEmpty(cityIdStr)) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    importSupplierVo.setMannageCity(cityIdStr);
                } catch (Exception e) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }

                // 状态
                importSupplierVo.setStatusVal(PoiUtil.getValue(row.getCell(4)));
                if (StringUtils.isEmpty(importSupplierVo.getStatusVal())) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                try {
                    List<SysDictEntity> enableDictList = dictCache.getDictByTypeNameFromCache(USER_STATUS);
                    int key = getCodeByLabel(enableDictList, importSupplierVo.getStatusVal());
                    if (key < NEG_ZERO) {
                        appendLog(importFail, importFailCount, j);
                        continue;
                    }
                    importSupplierVo.setStatusKey(key);
                } catch (Exception e) {
                    appendLog(importFail, importFailCount, j);
                    continue;
                }
                importSucceedCount.incrementAndGet();
                supplierVos.add(importSupplierVo);
            }
        });

        // 空白Excel直接结束
        List<ImportSupplierVo> distinctList = supplierVos.stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(distinctList) && isAllLessThanZero(matchFailCount, matchSucceedCount, importFailCount, importSucceedCount)) {
            return NEG_ONE;
        }
        // 有相同的用户不同的其他属性，直接结束
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
                Integer.parseInt(CommonEnum.LOG_TYPE_SUPPLIER_BATCH_UPDATE.getCode()));

        if (CollectionUtils.isEmpty(distinctList)) {
            return count;
        }

        logger.info("***PRO***准备保存供应商批量修改导入数据 {}", distinctList.size());
        importService.supplierImport(distinctList, operator);
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            distinctList.forEach(account -> {
                SupplierAccountEntity accountEntity = new SupplierAccountEntity();
                BeanUtils.copyProperties(account, accountEntity);
                EmployCompanyEntity companyEntity = employCompanyMapper.queryCompanyById(account.getCompanyId());
                accountEntity.setMannageCity(supplierVos.stream()
                        .filter(s -> objEquals(s.getUserId(), account.getUserId(), String::equals))
                        .map(ImportSupplierVo::getMannageCity).findFirst().orElse(null));
                syncSupplier(accountEntity, companyEntity.getSecondCategory(), operator);
            });
        }
        return count;
    }

    private ArrayList<ImportSupplierVo> distinctField(List<ImportSupplierVo> distinctList) {
        return distinctList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                () -> new TreeSet<>(Comparator.comparing(s ->
                        s.getCompanyName() + SPLIT_AND + s.getEmail() + SPLIT_AND + s.getMannageCity() + SPLIT_AND +
                                s.getStatusVal()))), ArrayList::new));
    }

    private String getCityIdStr(String chCityStr) throws Exception {
        List<CityLevelEntity> cityEntityList = cityCache.getAllCityFromCache();
        List<String> inputCity = getList(chCityStr, SPLIT_DAWN, str -> str);

        List<String> cityIdList = inputCity.stream().distinct()
                .map(cityInp -> cityEntityList.stream().filter(city -> city.getCity().equals(cityInp)).findFirst().orElse(null))
                .filter(Objects::nonNull).map(CityLevelEntity::getCityId).map(String::valueOf).collect(Collectors.toList());
        return cityIdList.stream().reduce((x, y) -> x + SPLIT_DAWN + y).orElse(STRING_EMPTY);
    }
}

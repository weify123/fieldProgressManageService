package com.autoyol.field.progress.manage.service;

import com.alibaba.fastjson.JSON;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.AppUserEntity;
import com.autoyol.field.progress.manage.entity.EmployCompanyEntity;
import com.autoyol.field.progress.manage.entity.SupplierAccountEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.AppUserMapper;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.mapper.SupplierAccountMapper;
import com.autoyol.field.progress.manage.request.company.CategoryVo;
import com.autoyol.field.progress.manage.request.company.CompanyAddCategoryReqVo;
import com.autoyol.field.progress.manage.request.company.CompanyPageReqVo;
import com.autoyol.field.progress.manage.request.company.CompanyUpdateReqVo;
import com.autoyol.field.progress.manage.response.company.CompanyCategoryRespVo;
import com.autoyol.field.progress.manage.response.company.CompanySecondCategory;
import com.autoyol.field.progress.manage.response.company.CompanyVo;
import com.autoyol.sop.util.mq.TenantCreateMQ;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class CompanyService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    DictCache dictCache;

    @Resource
    AppUserMapper appUserMapper;

    @Resource
    private EmployCompanyMapper employCompanyMapper;

    @Resource
    SupplierAccountMapper supplierAccountMapper;

    @Resource
    MsgService msgService;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    /**
     * 企业版同步
     *
     * @param tenant
     */
    public void saveOrUpdateCompany(TenantCreateMQ tenant) {
        EmployCompanyEntity companyEntity = employCompanyMapper.getObjByName(tenant.getTenantName());
        if (Objects.isNull(companyEntity)) {

            EmployCompanyEntity record = new EmployCompanyEntity();
            record.setTenantId(tenant.getId());
            record.setFirstCategory(WB);
            record.setSecondCategory(tenant.getTenantName());
            record.setEmail(tenant.getEmail());
            record.setEffectived(INT_ONE);
            employCompanyMapper.insertSelective(record);
            syncCompany(record, record.getId(), record.getCreateOp());
            return;
        }

        EmployCompanyEntity record = new EmployCompanyEntity();
        record.setId(companyEntity.getId());
        record.setEmail(tenant.getEmail());
        employCompanyMapper.updateById(record);
        syncCompany(record, record.getId(), companyEntity.getUpdateOp());
    }

    public List<CompanyVo> findCompanyByCond(CompanyPageReqVo reqVo) throws Exception {
        List<EmployCompanyEntity> entities = employCompanyMapper.selectAll();
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.EMPTY_LIST;
        }
        return entities.stream().filter(company -> hitSearch(reqVo.getFirstCategory(), company.getFirstCategory(), String::equals))
                .filter(company -> hitSearch(reqVo.getSecondCategory(), company.getSecondCategory(), String::equals))
                .filter(company -> hitSearch(reqVo.getEffectivedKey(), company.getEffectived(), Integer::equals))
                .sorted(Comparator.comparing(EmployCompanyEntity::getCreateTime).reversed())
                .map(this::buildCompanyPageRespVo)
                .collect(Collectors.toList());
    }

    public CompanyCategoryRespVo queryCompanyCategory(String first) throws Exception {
        CompanyCategoryRespVo categoryRespVo = new CompanyCategoryRespVo();
        List<EmployCompanyEntity> entities = employCompanyMapper.selectAll();

        if (CollectionUtils.isEmpty(entities)) {
            categoryRespVo.setCategoryList(Collections.EMPTY_LIST);
            return categoryRespVo;
        }

        Map<String, List<EmployCompanyEntity>> map = entities.stream()
                .filter(company -> !objEquals(company.getFirstCategory(), first, String::equals))
                .filter(company -> objEquals(company.getEffectived(), INT_ONE, Integer::equals))
                .collect(Collectors.groupingBy(EmployCompanyEntity::getFirstCategory));
        setCategoryList(categoryRespVo, map);
        return categoryRespVo;
    }

    /**
     * 仁云同步field
     *
     * @param map
     */
    public void saveOrUpdate(Map map) {
        EmployCompanyEntity companyEntity = JSON.parseObject(JSON.toJSONString(map), EmployCompanyEntity.class);
        companyEntity.setRenYunId((Integer) map.get("companyId"));
        EmployCompanyEntity entity = employCompanyMapper.getObjByName(companyEntity.getSecondCategory());
        if (Objects.isNull(entity)) {
            companyEntity.setCreateOp((String) map.get("operator"));
            employCompanyMapper.insertSelective(companyEntity);
            return;
        }
        companyEntity.setUpdateOp((String) map.get("operator"));
        employCompanyMapper.updateById(companyEntity);
    }

    public int addCompanyCategory(CompanyAddCategoryReqVo reqVo) throws Exception {
        EmployCompanyEntity companyEntity = employCompanyMapper.getObjByName(reqVo.getSecondCategory());
        if (Objects.nonNull(companyEntity)) {
            return NEG_ONE;
        }
        EmployCompanyEntity record = new EmployCompanyEntity();
        record.setFirstCategory(reqVo.getFirstCategory());
        record.setSecondCategory(reqVo.getSecondCategory());
        record.setEmail(reqVo.getEmail());
        record.setEffectived(reqVo.getEffectivedKey());
        record.setCreateOp(reqVo.getHandleUser());
        int count = employCompanyMapper.insertSelective(record);
        if (count <= NEG_ZERO) {
            return count;
        }
        syncCompany(record, record.getId(), record.getCreateOp());
        return count;
    }

    public int updateCompanyCategory(CompanyUpdateReqVo reqVo) throws Exception {
        EmployCompanyEntity companyEntity = employCompanyMapper.getObjByName(reqVo.getSecondCategory());
        if (Objects.nonNull(companyEntity) && !objEquals(companyEntity.getId(), reqVo.getCompanyId(), Integer::equals)) {
            return NEG_ONE;
        }
        if (isExistNonDisableUser(reqVo)) {
            return NEG_TOW;
        }

        SupplierAccountEntity supplierAccountEntity = new SupplierAccountEntity();
        supplierAccountEntity.setCompanyId(reqVo.getCompanyId());
        supplierAccountEntity.setIsEnable(NEG_ZERO);
        List<SupplierAccountEntity> supplierAccountEntityList = supplierAccountMapper.queryCond(supplierAccountEntity);
        if (!CollectionUtils.isEmpty(supplierAccountEntityList)) {
            return NEG_THREE;
        }

        EmployCompanyEntity record = new EmployCompanyEntity();
        record.setFirstCategory(reqVo.getFirstCategory());
        record.setSecondCategory(reqVo.getSecondCategory());
        record.setEmail(reqVo.getEmail());
        record.setEffectived(reqVo.getEffectivedKey());
        record.setUpdateOp(reqVo.getHandleUser());
        int count = employCompanyMapper.updateById(record);
        if (count <= NEG_ZERO) {
            return count;
        }
        syncCompany(record, companyEntity.getId(), record.getUpdateOp());
        return count;
    }

    /**
     * field同步仁云
     *
     * @param record
     * @param operator
     */
    private void syncCompany(EmployCompanyEntity record, Integer companyId, String operator) {
        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            Map<String, Object> map = msgService.buildMap();
            map.put("companyId", companyId);
            map.put("renYunId", record.getRenYunId());
            map.put("firstCategory", record.getFirstCategory());
            map.put("secondCategory", record.getSecondCategory());
            map.put("email", record.getEmail());
            map.put("effective", record.getEffectived());
            map.put("operator", operator);
            msgService.sendMq(sqlServer_company_mq_exchange, sqlServer_company_mq_routing_key, INT_FOUR, map);
        }
    }

    private boolean isExistNonDisableUser(CompanyUpdateReqVo reqVo) throws Exception {
        if (Objects.nonNull(reqVo.getEffectivedKey()) && reqVo.getEffectivedKey() == NEG_ZERO) {
            AppUserEntity userEntity = new AppUserEntity();
            userEntity.setEmploymentCompanyId(reqVo.getCompanyId());
            userEntity.setCurStatus(String.valueOf(CacheConstraint.NEG_ONE));
            List<AppUserEntity> list = appUserMapper.queryAppUserByCond(userEntity);
            return !CollectionUtils.isEmpty(list);
        }
        return false;
    }

    public CompanyVo queryCompanyById(Integer companyId) {
        EmployCompanyEntity entity = employCompanyMapper.queryCompanyById(companyId);
        CompanyVo respVo = new CompanyVo();
        if (Objects.isNull(entity)){
            return respVo;
        }
        BeanUtils.copyProperties(entity, respVo);
        try {
            List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(EFFECTIVE_TYPE);
            if (!CollectionUtils.isEmpty(dictList)) {
                SysDictEntity dict = dictList.stream().filter(d -> hitSearch(entity.getEffectived(), d.getCode(), Integer::equals))
                        .findFirst().orElse(new SysDictEntity());
                respVo.setEffectivedKey(dict.getCode());
                respVo.setEffectivedVal(dict.getLabel());
            }
        } catch (Exception e) {
            logger.error("dictCache 查询EFFECTIVE_TYPE异常 {}", e);
            Cat.logError("dictCache 查询EFFECTIVE_TYPE异常 {}", e);
        }
        return respVo;
    }

    private void setCategoryList(CompanyCategoryRespVo categoryRespVo, Map<String, List<EmployCompanyEntity>> map) {
        List<CategoryVo> vos = Lists.newArrayList();
        map.forEach((k, v) -> {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setFirstCategory(k);
            categoryVo.setSecondCategory(getSecondCategory(v));
            vos.add(categoryVo);
        });
        categoryRespVo.setCategoryList(vos);
    }

    private List<CompanySecondCategory> getSecondCategory(List<EmployCompanyEntity> v) {
        return getObjOrDefault(v, entity -> entity.stream()
                .filter(company -> StringUtils.isNotBlank(company.getSecondCategory()))
                .map(company -> {
                    CompanySecondCategory category = new CompanySecondCategory();
                    category.setCompanyId(company.getId());
                    category.setSecondCategory(company.getSecondCategory());
                    return category;
                }).collect(Collectors.toList()), Collections.EMPTY_LIST);
    }


    private CompanyVo buildCompanyPageRespVo(EmployCompanyEntity company) {
        CompanyVo respVo = new CompanyVo();
        BeanUtils.copyProperties(company, respVo);
        respVo.setCompanyId(company.getId());
        try {
            List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(EFFECTIVE_TYPE);
            if (!CollectionUtils.isEmpty(dictList)) {
                SysDictEntity entity = dictList.stream().filter(dict -> hitSearch(company.getEffectived(), dict.getCode(), Integer::equals))
                        .findFirst().orElse(new SysDictEntity());
                respVo.setEffectivedKey(entity.getCode());
                respVo.setEffectivedVal(entity.getLabel());
            }
        } catch (Exception e) {
            logger.error("dictCache 查询EFFECTIVE_TYPE异常 {}", e);
            Cat.logError("dictCache 查询EFFECTIVE_TYPE异常 {}", e);
        }
        return respVo;
    }
}

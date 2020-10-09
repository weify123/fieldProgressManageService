package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.EmployCompanyEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployCompanyMapper {

    int insertSelective(EmployCompanyEntity record);

    int updateById(EmployCompanyEntity record);

    EmployCompanyEntity queryCompanyByTenantId(@Param("tenantId") Integer tenantId);

    EmployCompanyEntity queryCompanyById(@Param("id") Integer id);

    List<EmployCompanyEntity> selectAll();

    EmployCompanyEntity getObjByName(@Param("name") String name);

}
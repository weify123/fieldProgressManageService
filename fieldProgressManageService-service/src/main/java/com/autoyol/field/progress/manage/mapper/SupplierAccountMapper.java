package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.SupplierAccountEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SupplierAccountMapper {

    List<SupplierAccountEntity> queryCond(SupplierAccountEntity record);

    SupplierAccountEntity selectByPrimaryKey(@Param("id") Integer id);

    SupplierAccountEntity selectSupplierByName(@Param("userName") String userName);

    SupplierAccountEntity selectSupplierByUserId(@Param("userId") String userId);

    String getUserIdByUserName(@Param("userName") String userName);

    int updateById(SupplierAccountEntity record);

    int updateByUserId(SupplierAccountEntity record);

    int insert(SupplierAccountEntity record);

    int insertSelective(SupplierAccountEntity record);

    int batchUpdate(@Param("list") List<SupplierAccountEntity> list);
}
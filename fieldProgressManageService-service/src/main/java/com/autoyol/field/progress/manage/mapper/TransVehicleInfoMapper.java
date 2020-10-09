package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.TransVehicleInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransVehicleInfoMapper {

    TransVehicleInfoEntity queryByOrderNo(@Param("orderNo") String orderNo);

    List<TransVehicleInfoEntity> queryByOrderNoList(@Param("orderNoList") List<String> orderNoList);

    int updateByOrderNo(TransVehicleInfoEntity record);

    int insertSelective(TransVehicleInfoEntity record);
}
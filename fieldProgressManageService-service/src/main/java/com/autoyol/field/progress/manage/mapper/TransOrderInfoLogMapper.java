package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.TransOrderInfoLogEntity;
import org.apache.ibatis.annotations.Param;

public interface TransOrderInfoLogMapper {

    TransOrderInfoLogEntity queryByOrderNo(@Param("orderNo") String orderNo);

    int insert(TransOrderInfoLogEntity record);

    int insertSelective(TransOrderInfoLogEntity record);
}
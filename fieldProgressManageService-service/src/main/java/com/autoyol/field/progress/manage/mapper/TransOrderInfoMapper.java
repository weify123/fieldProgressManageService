package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.TransOrderInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransOrderInfoMapper {

    TransOrderInfoEntity queryByOrderNo(@Param("orderNo") String orderNo);

    List<TransOrderInfoEntity> queryByOrderNoList(@Param("orderNoList") List<String> orderNoList);

    int updateByOrderNo(TransOrderInfoEntity record);

    int insert(TransOrderInfoEntity record);

    int insertSelective(TransOrderInfoEntity record);
}
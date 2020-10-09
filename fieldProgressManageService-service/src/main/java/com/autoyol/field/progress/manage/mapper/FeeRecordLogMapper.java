package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.FeeRecordLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeeRecordLogMapper {

    FeeRecordLogEntity selectByPickDeliverIdAndFeeLabelId(FeeRecordLogEntity record);

    List<FeeRecordLogEntity> selectByPickDeliverId(@Param("pickDeliverFeeId") Integer pickDeliverId);

    int insert(FeeRecordLogEntity record);

    int batchInsert(@Param("list") List<FeeRecordLogEntity> list);

    int batchInsertLog(@Param("list") List<FeeRecordLogEntity> list);

    int insertSelective(FeeRecordLogEntity record);
}
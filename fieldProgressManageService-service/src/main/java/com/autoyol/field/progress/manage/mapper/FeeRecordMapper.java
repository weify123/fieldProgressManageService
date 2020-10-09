package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.FeeRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeeRecordMapper {

    List<FeeRecordEntity> queryByPickFeeId(FeeRecordEntity feeRecordEntity);

    int countByCond(FeeRecordEntity feeRecordEntity);

    int batchInsert(@Param("list") List<FeeRecordEntity> list);

    int batchUpdate(@Param("list") List<FeeRecordEntity> list);

    int batchSubmit(FeeRecordEntity record);

    int insert(FeeRecordEntity record);

    int insertSelective(FeeRecordEntity record);
}
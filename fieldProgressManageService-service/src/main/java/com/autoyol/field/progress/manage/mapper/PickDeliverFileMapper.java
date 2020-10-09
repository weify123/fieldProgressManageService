package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.PickDeliverFileEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PickDeliverFileMapper {

    int batchInsert(@Param("list") List<PickDeliverFileEntity> list);

    int batchDelete(@Param("list") List<PickDeliverFileEntity> list);

    int batchSubmit(PickDeliverFileEntity record);

    List<PickDeliverFileEntity> queryPickDeliverPic(PickDeliverFileEntity record);

    int insertSelective(PickDeliverFileEntity record);
}
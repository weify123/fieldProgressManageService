package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.NodeRemindConfEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NodeRemindConfMapper {
    List<NodeRemindConfEntity> findByCond(NodeRemindConfEntity record);

    NodeRemindConfEntity selectById(@Param("id") Integer id);

    int countExist(NodeRemindConfEntity record);

    int insert(NodeRemindConfEntity record);

    int insertSelective(NodeRemindConfEntity record);

    int updateById(NodeRemindConfEntity record);

}
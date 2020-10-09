package com.autoyol.field.progress.manage.mapper;

import com.autoyol.field.progress.manage.entity.RabbitMsgLogEntity;

import java.util.List;

public interface RabbitMsgLogMapper {

    List<RabbitMsgLogEntity> selectMqSending();

    List<RabbitMsgLogEntity> selectSupplierConsuming();

    int updateByMsgId(RabbitMsgLogEntity record);

    int insertSelective(RabbitMsgLogEntity record);
}
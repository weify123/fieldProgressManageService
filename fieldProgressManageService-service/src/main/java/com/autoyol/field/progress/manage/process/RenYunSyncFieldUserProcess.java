package com.autoyol.field.progress.manage.process;


import com.alibaba.fastjson.JSON;
import com.autoyol.field.progress.manage.entity.RabbitMsgLogEntity;
import com.autoyol.field.progress.manage.mapper.RabbitMsgLogMapper;
import com.autoyol.field.progress.manage.service.AppUserInfoService;
import com.autoyol.field.progress.manage.service.AttendanceService;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.getObjOrDefault;

@Component
public class RenYunSyncFieldUserProcess implements TaskProcess {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    RabbitMsgLogMapper rabbitMsgLogMapper;

    @Resource
    AppUserInfoService appUserInfoService;

    @Override
    @Async("taskExecutor")
    public void process(String msgId, String msg) {
        logger.info("***PRO*** msgId {}, msg {}", msgId, msg);
        Map map = JSON.parseObject(msg, Map.class);

        RabbitMsgLogEntity msgLogEntity = new RabbitMsgLogEntity();
        String msgIdStr = getObjOrDefault(msgId, Function.identity(), (String) map.get("messageId"));
        if (StringUtils.isEmpty(msgIdStr)) {
            return;
        }
        msgLogEntity.setMsgId(msgIdStr.replaceAll("-", ""));
        msgLogEntity.setMsg(msg);
        msgLogEntity.setExchange(sqlServer_field_user_mq_exchange);
        msgLogEntity.setRoutingKey(sqlServer_field_user_mq_routing_key);
        msgLogEntity.setTryCount(0);
        msgLogEntity.setStatus(INT_FOUR);
        msgLogEntity.setServiceType(INT_NINE);
        msgLogEntity.setCreateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
        rabbitMsgLogMapper.insertSelective(msgLogEntity);

        try {
            appUserInfoService.saveOrUpdate(map);
        } catch (Exception e) {
            logger.error("接收仁云车管家处理失败msg {},e {}", msg, map);
            return;
        }

        msgLogEntity.setStatus(INT_FIVE);
        msgLogEntity.setUpdateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
        rabbitMsgLogMapper.updateByMsgId(msgLogEntity);
    }

    @Override
    public void mailProcess(String msgName, String receiveId, String receiveNo, String operator, Map<String, Object> paramMap) {

    }
}

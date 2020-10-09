package com.autoyol.field.progress.manage.process;


import com.alibaba.fastjson.JSON;
import com.autoyol.field.progress.manage.entity.RabbitMsgLogEntity;
import com.autoyol.field.progress.manage.mapper.RabbitMsgLogMapper;
import com.autoyol.field.progress.manage.service.SupplierAccountService;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.autoyol.sop.util.RabbitConstants;
import com.autoyol.sop.util.enums.TenantTypeEnum;
import com.autoyol.sop.util.mq.UserCreateMQ;
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
import static com.autoyol.field.progress.manage.util.OprUtil.objEquals;

@Component
public class SupplierOprProcess implements TaskProcess {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    RabbitMsgLogMapper rabbitMsgLogMapper;

    @Resource
    SupplierAccountService supplierAccountService;

    @Override
    @Async("taskExecutor")
    public void process(String msgId, String msg) {

        UserCreateMQ user = JSON.parseObject(msg, UserCreateMQ.class);

        logger.info("***PRO*** 用户类型 {}, 是否管理员 {}", user.getTenantType(), user.getAdmin());
        if (!objEquals(TenantTypeEnum.LOGISTICS.getType(), user.getTenantType(), StringUtils::endsWithIgnoreCase)) {
            return;
        }

        RabbitMsgLogEntity msgLogEntity = new RabbitMsgLogEntity();
        String msgIdStr = getObjOrDefault(msgId, Function.identity(), user.getMessageId());
        if (StringUtils.isEmpty(msgIdStr)) {
            return;
        }
        msgLogEntity.setMsgId(msgIdStr.replaceAll("-", ""));
        msgLogEntity.setMsg(msg);
        msgLogEntity.setExchange(RabbitConstants.USER_EXCHANGE_NAME);
        msgLogEntity.setRoutingKey("sop.user.#");
        msgLogEntity.setTryCount(0);
        msgLogEntity.setStatus(INT_FOUR);
        msgLogEntity.setServiceType(INT_ONE);
        msgLogEntity.setCreateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
        rabbitMsgLogMapper.insertSelective(msgLogEntity);

        supplierAccountService.saveOrUpdate(user);

        msgLogEntity.setStatus(INT_FIVE);
        msgLogEntity.setUpdateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
        rabbitMsgLogMapper.updateByMsgId(msgLogEntity);
    }

    @Override
    public void mailProcess(String msgName, String receiveId, String receiveNo, String operator, Map<String, Object> paramMap) {

    }
}

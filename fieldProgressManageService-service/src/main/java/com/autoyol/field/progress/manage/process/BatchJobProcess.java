package com.autoyol.field.progress.manage.process;

import com.alibaba.fastjson.JSON;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.entity.MsgTemplateLogEntity;
import com.autoyol.field.progress.manage.entity.RabbitMsgLogEntity;
import com.autoyol.field.progress.manage.mapper.MsgTemplateLogMapper;
import com.autoyol.field.progress.manage.mapper.RabbitMsgLogMapper;
import com.autoyol.field.progress.manage.service.*;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.autoyol.field.progress.manage.util.OprUtil;
import com.autoyol.sop.util.mq.TenantCreateMQ;
import com.autoyol.sop.util.mq.UserCreateMQ;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;

@Component
public class BatchJobProcess implements TaskProcess {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${msg.try.count}")
    private long tryCount;

    @Resource
    RabbitMsgLogMapper rabbitMsgLogMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource
    SupplierAccountService supplierAccountService;

    @Resource
    CompanyService companyService;

    @Resource
    AttendanceService attendanceService;

    @Resource
    AppUserInfoService appUserInfoService;

    @Resource
    MsgTemplateLogMapper msgTemplateLogMapper;

    @Resource
    MsgService msgService;

    @Override
    @Async("taskExecutor")
    public void process(String msgId, String msg) {
        List<RabbitMsgLogEntity> consumingList = rabbitMsgLogMapper.selectSupplierConsuming();
        receiveProcess(consumingList);

        List<MsgTemplateLogEntity> msgTemplateLogList = msgTemplateLogMapper.selectMailSending();
        mailProcess(msgTemplateLogList);

        List<RabbitMsgLogEntity> rabbitMsgLogList = rabbitMsgLogMapper.selectMqSending();
        if (CollectionUtils.isEmpty(rabbitMsgLogList)) {
            return;
        }

        rabbitMsgLogList.forEach(rabbitMsg -> {
            logger.info("批量发送到rabbitmq,{}", rabbitMsg);
            rabbitMsg.setTryCount(rabbitMsg.getTryCount() + 1);
            if (rabbitMsg.getTryCount() < tryCount) {
                try {
                    rabbitTemplate.convertAndSend(rabbitMsg.getExchange(), rabbitMsg.getRoutingKey(),
                            JSON.parseObject(rabbitMsg.getMsg(), HashMap.class), new CorrelationData(rabbitMsg.getMsgId()));
                    rabbitMsg.setStatus(INT_ONE);
                } catch (AmqpException e) {
                    logger.error("mq消息重发失败 rabbitMsg={} e = {}", rabbitMsg, e);
                    Cat.logError("rabbitMsg[" + rabbitMsg + "]重发失败 异常 {}", e);
                }
            } else {
                rabbitMsg.setStatus(INT_TOW);
            }
            rabbitMsg.setUpdateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
            rabbitMsgLogMapper.updateByMsgId(rabbitMsg);
        });

    }

    private void receiveProcess(List<RabbitMsgLogEntity> consumingList) {
        // 供应商 企业版->流程
        consumeProcess(consumingList, INT_ONE, supplierAccountService, (str, service) -> {
            try {
                UserCreateMQ user = JSON.parseObject(str, UserCreateMQ.class);
                service.saveOrUpdate(user);
            } catch (Exception e) {
                logger.error("消费企业版供应商重试失败 str={} e = {}", str, e);
                Cat.logError("str[" + str + "]消费企业版供应商重试失败 异常 {}", e);
            }
        });
        // 公司 企业版->流程
        consumeProcess(consumingList, INT_THREE, companyService, (str, service) -> {
            try {
                TenantCreateMQ tenant = JSON.parseObject(str, TenantCreateMQ.class);
                service.saveOrUpdateCompany(tenant);
            } catch (Exception e) {
                logger.error("消费企业版公司重试失败 str={} e = {}", str, e);
                Cat.logError("str[" + str + "]消费企业版公司重试失败 异常 {}", e);
            }
        });

        // 公司同步 仁云->流程
        consumeProcess(consumingList, INT_FIVE, companyService, (str, service) -> {
            try {
                Map map = JSON.parseObject(str, Map.class);
                service.saveOrUpdate(map);
            } catch (Exception e) {
                logger.error("消费仁云公司重试失败 str={} e = {}", str, e);
                Cat.logError("str[" + str + "]消费仁云公司重试失败 异常 {}", e);
            }
        });
        // 供应商 仁云->流程
        consumeProcess(consumingList, INT_SEVEN, supplierAccountService, (str, service) -> {
            try {
                Map map = JSON.parseObject(str, Map.class);
                service.saveOrUpdate(map);
            } catch (Exception e) {
                logger.error("消费仁云同步供应商重试失败 str={} e = {}", str, e);
                Cat.logError("str[" + str + "]仁云同步供应商重试失败 异常 {}", e);
            }
        });
        // 考勤 仁云->流程
        consumeProcess(consumingList, INT_EIGHT, attendanceService, (str, service) -> {
            try {
                Map map = JSON.parseObject(str, Map.class);
                service.updateAttendance(map);
            } catch (Exception e) {
                logger.error("消费仁云同步考勤重试失败 str={} e = {}", str, e);
                Cat.logError("str[" + str + "]仁云同步考勤重试失败 异常 {}", e);
            }
        });
        // 车管家 仁云->流程
        consumeProcess(consumingList, INT_NINE, appUserInfoService, (str, service) -> {
            try {
                Map map = JSON.parseObject(str, Map.class);
                service.saveOrUpdate(map);
            } catch (Exception e) {
                logger.error("消费仁云同步车管家重试失败 str={} e = {}", str, e);
                Cat.logError("str[" + str + "]仁云同步车管家重试失败 异常 {}", e);
            }
        });
    }

    private <S> void consumeProcess(List<RabbitMsgLogEntity> consumingList, int serviceType, S service, BiConsumer<String, S> callMethod) {
        if (CollectionUtils.isEmpty(consumingList)) {
            return;
        }
        consumingList.stream()
                .filter(x -> OprUtil.objEquals(x.getServiceType(), serviceType, Integer::equals))
                .forEach(consuming -> {
                    logger.info("批量处理仁云同步失败rabbitmq consuming,{}", consuming);
                    consuming.setTryCount(consuming.getTryCount() + 1);
                    if (consuming.getTryCount() < tryCount) {
                        callMethod.accept(consuming.getMsg(), service);
                        consuming.setStatus(INT_FIVE);
                    } else {
                        consuming.setStatus(INT_SIX);
                    }
                    consuming.setUpdateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
                    rabbitMsgLogMapper.updateByMsgId(consuming);
                });
    }

    private void mailProcess(List<MsgTemplateLogEntity> msgTemplateLogList) {
        if (CollectionUtils.isEmpty(msgTemplateLogList)) {
            return;
        }

        msgTemplateLogList.forEach(msg -> {
            logger.info("批量发送邮件 msg,{}", msg);
            msg.setTryCount(msg.getTryCount() + 1);
            if (msg.getTryCount() < tryCount) {
                try {
                    msgService.sendMail(msg.getReceiverNum(), msg.getMsgSubject(), msg.getMsgContent());
                    msg.setSendStatus(INT_ONE);
                } catch (Exception e) {
                    Cat.logError("msg[" + msg + "]批量邮件信息发送失败 异常 {}", e);
                    logger.error("批量邮件信息发送失败msg={},e={}", msg, e);
                }
            } else {
                msg.setSendStatus(CacheConstraint.INT_TOW);
            }
            msg.setUpdateOp(CacheConstraint.SYS);
            msgTemplateLogMapper.updateById(msg);
        });
    }

    @Override
    public void mailProcess(String msgName, String receiveId, String receiveNo, String operator, Map<String, Object> paramMap) {

    }
}

package com.autoyol.field.progress.manage.service;


import com.alibaba.fastjson.JSON;
import com.autoyol.field.progress.manage.entity.RabbitMsgLogEntity;
import com.autoyol.field.progress.manage.mapper.RabbitMsgLogMapper;
import com.autoyol.field.progress.manage.process.TaskProcess;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.NEG_ZERO;

@Component
public class MsgService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    JavaMailSender mailSender;

    @Resource
    RabbitMsgLogMapper rabbitMsgLogMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    @Resource(name = "mailProcess")
    TaskProcess taskProcess;

    @Value("${mail.from.name}")
    private String fromName;

    @Value("${mail.from.num}")
    private String fromNum;

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromName);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }

    public void sendMq(String exchange, String routingKey, int serviceType, Map<String, Object> paramMap) {
        String json = JSON.toJSONString(paramMap);
        RabbitMsgLogEntity msgLogEntity = new RabbitMsgLogEntity();
        msgLogEntity.setMsgId((String) paramMap.get("messageId"));
        msgLogEntity.setMsg(json);
        msgLogEntity.setExchange(exchange);
        msgLogEntity.setRoutingKey(routingKey);
        msgLogEntity.setStatus(NEG_ZERO);
        msgLogEntity.setServiceType(serviceType);
        msgLogEntity.setCreateTime(LocalDateUtil.convertToDate(LocalDateTime.now()));
        rabbitMsgLogMapper.insertSelective(msgLogEntity);
        rabbitTemplate.convertAndSend(exchange, routingKey, paramMap, new CorrelationData(msgLogEntity.getMsgId()));
    }

    public Map<String, Object> buildMap(String msgName, String to, String desc) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("textCode", msgName);
        paramMap.put("mobile", to);
        paramMap.put("messageId", UUID.randomUUID().toString().replaceAll("-", ""));
        paramMap.put("message", desc);
        return paramMap;
    }

    public Map<String, Object> buildMap() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("messageId", UUID.randomUUID().toString().replaceAll("-", ""));
        return paramMap;
    }

    public void sendMail(String msgName, String receiveId, String receiveNo, String operator, Map<String, Object> paramMap) {
        logger.info("信息模板 msgName = {}，to = {}", msgName, receiveNo);
        taskProcess.mailProcess(msgName, receiveId, receiveNo, operator, paramMap);
    }
}

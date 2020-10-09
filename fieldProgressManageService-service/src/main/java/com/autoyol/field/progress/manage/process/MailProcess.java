package com.autoyol.field.progress.manage.process;

import com.autoyol.field.progress.manage.entity.MsgTemplateEntity;
import com.autoyol.field.progress.manage.entity.MsgTemplateLogEntity;
import com.autoyol.field.progress.manage.mapper.MsgTemplateLogMapper;
import com.autoyol.field.progress.manage.mapper.MsgTemplateMapper;
import com.dianping.cat.Cat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.getList;
import static com.autoyol.field.progress.manage.util.OprUtil.translateTemplate;

@Component
public class MailProcess implements TaskProcess {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    MsgTemplateLogMapper msgTemplateLogMapper;

    @Resource
    MsgTemplateMapper msgTemplateMapper;

    @Resource
    JavaMailSender mailSender;

    @Value("${mail.from.name}")
    private String fromName;

    @Value("${mail.from.num}")
    private String fromNum;

    @Override
    @Async("taskExecutor")
    public void mailProcess(String msgName, String receiveId, String receiveNo, String operator, Map<String, Object> paramMap) {
        logger.info("信息模板 msgName = {}，to = {}", msgName, receiveNo);
        MsgTemplateEntity templateEntity = getMsgTemplate(msgName, paramMap);
        if (Objects.isNull(templateEntity) || StringUtils.isEmpty(templateEntity.getMsgType())) {
            logger.warn("信息模板为空 msgName = {}，receiveNo = {}，templateEntity = {}", msgName, receiveNo, templateEntity);
            return;
        }
        MsgTemplateLogEntity templateLogEntity = buildMsgTemplateLogEntity(msgName, receiveId, receiveNo, operator);

        if (getList(templateEntity.getMsgType(), SPLIT_DAWN, Integer::parseInt).contains(INT_ONE)) {
            templateLogEntity.setMsgType(String.valueOf(INT_ONE));
            templateLogEntity.setMsgSubject(templateEntity.getMsgSubject());
            templateLogEntity.setMsgContent(templateEntity.getMsgContent());
            msgTemplateLogMapper.insertSelective(templateLogEntity);
        }

        try {
            sendMail(receiveNo, templateLogEntity.getMsgSubject(), templateEntity.getMsgContent());
        } catch (Exception e) {
            Cat.logError("信息发送失败 异常 {}", e);
            logger.error("信息发送失败,e={}", e);
        }
        templateLogEntity.setSendStatus(INT_ONE);
        templateLogEntity.setUpdateOp(operator);
        msgTemplateLogMapper.updateById(templateLogEntity);
    }

    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromName);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }

    private MsgTemplateLogEntity buildMsgTemplateLogEntity(String msgName, String receiveId, String receiveNo, String operator) {
        MsgTemplateLogEntity templateLogEntity = new MsgTemplateLogEntity();
        templateLogEntity.setMsgName(msgName);
        templateLogEntity.setBelongSys(NEG_ZERO);
        templateLogEntity.setSendName(fromName);
        templateLogEntity.setSendNum(fromNum);
        templateLogEntity.setReceiverId(receiveId);
        templateLogEntity.setReceiverNum(receiveNo);
        templateLogEntity.setSendStatus(NEG_ZERO);
        templateLogEntity.setCreateOp(operator);
        return templateLogEntity;
    }

    private MsgTemplateEntity getMsgTemplate(String msgName, Map<String, Object> paramMap) {
        MsgTemplateEntity msgTemplateEntity = new MsgTemplateEntity();
        msgTemplateEntity.setMsgName(msgName);
        msgTemplateEntity.setBelongSys(NEG_ZERO);
        MsgTemplateEntity templateEntity = msgTemplateMapper.selectByCond(msgTemplateEntity);
        if (Objects.isNull(templateEntity) || StringUtils.isEmpty(templateEntity.getMsgContent())) {
            return null;
        }
        templateEntity.setMsgContent(translateTemplate(templateEntity.getMsgContent(), paramMap));
        return templateEntity;
    }

    @Override
    public void process(String msgId, String msg) {

    }
}

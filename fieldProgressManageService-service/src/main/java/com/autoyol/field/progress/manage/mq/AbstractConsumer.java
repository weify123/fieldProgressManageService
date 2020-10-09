package com.autoyol.field.progress.manage.mq;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class AbstractConsumer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void consumeProcess(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        String msgId = message.getMessageProperties().getMessageId();
        logger.info("收到消息 msg: {}, msgId: {}", msg, msgId);
        process(msgId, msg);
        MessageProperties properties = message.getMessageProperties();
        channel.basicAck(properties.getDeliveryTag(), false);
    }

    protected void process(String msgId, String msg){}
}

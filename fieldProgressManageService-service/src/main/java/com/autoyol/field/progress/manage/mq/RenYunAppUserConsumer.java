package com.autoyol.field.progress.manage.mq;

import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.process.TaskProcess;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
public class RenYunAppUserConsumer extends AbstractConsumer{

    @Resource(name = "renYunSyncFieldUserProcess")
    TaskProcess taskProcess;

    @Override
    @RabbitListener(queues = CacheConstraint.sqlServer_field_user_mq_queue, containerFactory = "rabbitListenerContainerFactory")
    protected void consumeProcess(Message message, Channel channel) throws IOException {
        super.consumeProcess(message, channel);
    }

    @Override
    protected void process(String msgId, String msg) {
        taskProcess.process(msgId, msg);
    }
}

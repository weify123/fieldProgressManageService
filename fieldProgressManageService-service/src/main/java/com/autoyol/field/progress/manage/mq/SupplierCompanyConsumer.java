package com.autoyol.field.progress.manage.mq;

import com.autoyol.field.progress.manage.process.TaskProcess;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static com.autoyol.field.progress.manage.mq.MQConstraint.AUTO_TENANT_OPR_QUEUE;

@Component
public class SupplierCompanyConsumer extends AbstractConsumer{

    @Resource(name = "tenantProcess")
    TaskProcess taskProcess;

    @Override
    @RabbitListener(queues = AUTO_TENANT_OPR_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    protected void consumeProcess(Message message, Channel channel) throws IOException {
        super.consumeProcess(message, channel);
    }

    @Override
    protected void process(String msgId, String msg) {
        taskProcess.process(msgId, msg);
    }
}

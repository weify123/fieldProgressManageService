package com.autoyol.field.progress.manage.job;

import com.autoyol.field.progress.manage.process.TaskProcess;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@JobHandler("msgJob")
public class MsgJob extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "batchJobProcess")
    TaskProcess taskProcess;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        logger.info("msgJob execute start");
        try {
            taskProcess.process(null, null);
        } catch (Exception e) {
            logger.info("msgJob execute Exception {}", e);
            XxlJobLogger.log("msgJob execute Exception. msg: {}", e);
            return FAIL;
        }
        logger.info("msgJob execute end");
        return SUCCESS;
    }
}

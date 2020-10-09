package com.autoyol.field.progress.manage.job;

import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.service.AttendanceService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@JobHandler("attendanceJob")
public class AttendanceJob extends IJobHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AttendanceService attendanceService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        logger.info("attendanceJob execute start");
        try {
            attendanceService.batchInsertAllUserThirtyAttendance(CacheConstraint.SYS, null);
        } catch (Exception e) {
            logger.info("attendanceJob execute Exception {}",e);
            XxlJobLogger.log("attendanceJob execute Exception. msg: {}", e);
            return FAIL;
        }
        logger.info("attendanceJob execute end");
        return SUCCESS;
    }
}

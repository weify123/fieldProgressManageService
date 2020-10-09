package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.request.schedulelog.BaseReqVo;
import com.autoyol.field.progress.manage.response.schedulelog.ScheduleLogVo;
import com.autoyol.field.progress.manage.response.schedulelog.SchedulePageRespVo;
import com.autoyol.field.progress.manage.service.ScheduleStatusLogService;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/console/take/back")
@AutoDocVersion(version = "取送车调度状态日志")
public class OrderScheduleStatusLogController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    ScheduleStatusLogService scheduleStatusLogService;

    @RequestMapping(value = "/schedule/status/log", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询取送车调度状态日志", description = "根据取送车订单号查询调度状态日志", response = SchedulePageRespVo.class)
    public ResponseData<SchedulePageRespVo> scheduleStatusLog(@Validated BaseReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  scheduleStatusLog request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            List<ScheduleLogVo> list = scheduleStatusLogService.queryObjListByOrderId(reqVo.getPickDeliverOrderNo());
            logger.info("***RESP*** 返回list is [{}]", list);
            SchedulePageRespVo listRespVo = new SchedulePageRespVo();
            listRespVo.setList(list);
            return ResponseData.success(listRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** scheduleStatusLog error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询取送车调度状态日志 异常 {}", e);
            return ResponseData.error();
        }
    }
}

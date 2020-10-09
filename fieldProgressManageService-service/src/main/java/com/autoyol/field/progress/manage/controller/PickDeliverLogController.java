package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.request.tackback.PickDeliverLogPageReqVo;
import com.autoyol.field.progress.manage.response.tackback.PickDeliverPageRespVo;
import com.autoyol.field.progress.manage.service.PickDeliverLogService;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/console/take/back")
@AutoDocVersion(version = "取送车信息变更日志")
public class PickDeliverLogController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PickDeliverLogService pickDeliverLogService;

    @RequestMapping(value = "/query/change/log", method = RequestMethod.GET)
    @AutoDocMethod(value = "取送车信息变更日志", description = "根据取送车订单号查询调度状态日志", response = PickDeliverPageRespVo.class)
    public ResponseData<PickDeliverPageRespVo> queryChangeLog(@Validated PickDeliverLogPageReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  changeLog request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            return ResponseData.success(pickDeliverLogService.queryChangeLog(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** scheduleStatusLog error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]取送车信息变更日志 异常 {}", e);
            return ResponseData.error();
        }
    }
}

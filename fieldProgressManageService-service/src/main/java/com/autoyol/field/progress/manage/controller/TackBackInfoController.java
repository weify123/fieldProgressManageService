package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.request.tackback.*;
import com.autoyol.field.progress.manage.response.tackback.TackBackCanOprStatusRespVo;
import com.autoyol.field.progress.manage.response.tackback.TackBackFileRespVo;
import com.autoyol.field.progress.manage.service.TackBackInfoService;
import com.dianping.cat.Cat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.DATE_NO_SECOND;
import static com.autoyol.field.progress.manage.cache.CacheConstraint.DATE_YYYY_MM_DD_CONTACT;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.parse;

@RestController
@RequestMapping("/console/take/back")
@AutoDocVersion(version = "取还车信息变更")
public class TackBackInfoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    TackBackInfoService tackBackInfoService;

    @RequestMapping(value = "/pickInfo/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "更新取车信息", description = "更新取车信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> pickInfoUpdate(@Validated @RequestBody TackBackPickInfoReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  pickInfoUpdate request param is [{}]", reqVo);

        try {
            Date pickTime = checkAndGetDate(reqVo, bindingResult);
            tackBackInfoService.pickInfoUpdate(reqVo, pickTime);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** deliverInfoUpdate TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** pickInfoUpdate error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]更新取车信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/deliverInfo/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "更新送车信息", description = "更新送车信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> deliverInfoUpdate(@Validated @RequestBody TackBackDeliverInfoReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  deliverInfoUpdate request param is [{}]", reqVo);
        ResponseData<Integer> x = validFieldAndUser(reqVo, bindingResult);
        if (Objects.nonNull(x)) {
            return x;
        }

        Date pickTime;
        Date delayDeliverTime = null;
        try {
            pickTime = parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
            if (StringUtils.isNotEmpty(reqVo.getDelayDeliverTime())) {
                delayDeliverTime = parse(reqVo.getDelayDeliverTime(), DATE_NO_SECOND);
            }
        } catch (Exception e) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.DATE_FORMAT_ERROR);
        }

        try {
            tackBackInfoService.deliverInfoUpdate(reqVo, pickTime, delayDeliverTime);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** deliverInfoUpdate TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** deliverInfoUpdate error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]更新送车信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/query/pickDeliverPic", method = RequestMethod.GET)
    @AutoDocMethod(value = "查看照片", description = "查看照片", response = TackBackFileRespVo.class)
    public ResponseData<TackBackFileRespVo> queryPickDeliverPic(@Validated PickDeliverPicReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  queryPickDeliverPic request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            return ResponseData.success(tackBackInfoService.queryPickDeliverPic(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** queryPickDeliverPic error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查看照片 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/query/pickDeliverRecord", method = RequestMethod.GET)
    @AutoDocMethod(value = "查看录音", description = "查看录音", response = TackBackFileRespVo.class)
    public ResponseData<TackBackFileRespVo> queryPickDeliverRecord(@Validated PickDeliverPicReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  queryPickDeliverRecord request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            return ResponseData.success(tackBackInfoService.queryPickDeliverRecord(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** queryPickDeliverRecord error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查看录音 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/orderInfo/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "更新交易订单信息", description = "更新交易订单信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> orderInfoUpdate(@Validated @RequestBody TransOrderReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  orderInfoUpdate request param is [{}]", reqVo);

        try {
            Date pickTime = checkAndGetDate(reqVo, bindingResult);
            tackBackInfoService.orderInfoUpdate(reqVo, pickTime);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** orderInfoUpdate TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** orderInfoUpdate error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]更新交易订单信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/feeInfo/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "更新费用信息", description = "更新费用信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> feeInfoUpdate(@Validated @RequestBody TackBackFeeInfoReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  feeInfoUpdate request param is [{}]", reqVo);

        try {
            Date pickTime = checkAndGetDate(reqVo, bindingResult);
            tackBackInfoService.feeInfoUpdate(reqVo, pickTime);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** feeInfoUpdate TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** feeInfoUpdate error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]更新费用信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/scheduleInfo/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "更新调度信息", description = "更新调度信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> scheduleInfoUpdate(@Validated @RequestBody TackBackScheduleInfoReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  scheduleInfoUpdate request param is [{}]", reqVo);

        try {
            Date pickTime = checkAndGetDate(reqVo, bindingResult);
            tackBackInfoService.scheduleInfoUpdate(reqVo, pickTime);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** scheduleInfoUpdate TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** scheduleInfoUpdate error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]更新调度信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/query/canOprStatus", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询可操作状态", description = "查询可操作状态", response = TackBackCanOprStatusRespVo.class)
    public ResponseData<TackBackCanOprStatusRespVo> queryCanOprStatus() {
        try {
            return ResponseData.success(tackBackInfoService.queryCanOprStatus());
        } catch (Exception e) {
            logger.error("***RESP*** queryCanOprStatus error:{}", e);
            Cat.logError("查询可操作状态 异常 {}", e);
            return ResponseData.error();
        }
    }
}

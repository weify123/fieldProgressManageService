package com.autoyol.field.progress.manage.controller;

import com.alibaba.fastjson.JSON;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.constraint.FieldErrorCode;
import com.autoyol.field.progress.manage.exception.OrderException;
import com.autoyol.field.progress.manage.request.order.AddPickDeliverReqVo;
import com.autoyol.field.progress.manage.request.order.CancelTransOrderReqVo;
import com.autoyol.field.progress.manage.request.order.UpdateTransOrderReqVo;
import com.autoyol.field.progress.manage.service.OrderProcessService;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.NEG_ONE;
import static com.autoyol.field.progress.manage.cache.CacheConstraint.NEG_ZERO;

@RestController
@RequestMapping("/AotuInterface")
@AutoDocVersion(version = "交易订单操作")
public class TransOrderController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    OrderProcessService orderProcessService;

    @PostMapping(value = "/addflowprocessdata")
    @AutoDocMethod(value = "生成取送车订单", description = "生成取送车订单", response = String.class)
    public String addFlowProcessData(AddPickDeliverReqVo reqVo) {
        logger.info("***REQ***  addFlowProcessData request param is [{}]", reqVo);
        ResponseData responseData = ResponseData.success();
        int count = 0;
        try {
            count = orderProcessService.addOrderProcess(reqVo);
            logger.info("***PRO*** addFlowProcessData 返回count is [{}]", count);
        } catch (OrderException e) {
            logger.error("***RESP*** addFlowProcessData error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]生成取送车订单 异常 {}", e);
            responseData = ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** addFlowProcessData error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]生成取送车订单 异常 {}", e);
            responseData = ResponseData.error();
        }

        return JSON.toJSONString(responseData);
    }

    @PostMapping(value = "/cancelfloworder")
    @AutoDocMethod(value = "订单取消", description = "订单取消", response = String.class)
    public String cancelFlowOrder(CancelTransOrderReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  cancelFlowOrder request param is [{}]", reqVo);
        ResponseData responseData = ResponseData.success();
        if (bindingResult.hasErrors()) {
            responseData = validate(bindingResult);
        }
        int count = 0;
        try {
            count = orderProcessService.cancelFlowOrder(reqVo);
            logger.info("***PRO*** cancelFlowOrder 返回count is [{}]", count);
            if (count == NEG_ONE) {
                responseData = ResponseData.createErrorCodeResponse(FieldErrorCode.ORDER_NOT_EXIST.getCode(), FieldErrorCode.ORDER_NOT_EXIST.getText());
            }
            if (count == NEG_ZERO) {
                responseData = ResponseData.createErrorCodeResponse(FieldErrorCode.TACK_BACK_ORDER_NOT_EXIST.getCode(), FieldErrorCode.TACK_BACK_ORDER_NOT_EXIST.getText());
            }
        } catch (Exception e) {
            logger.error("***RESP*** cancelFlowOrder error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]订单取消 异常 {}", e);
            responseData = ResponseData.error();
        }

        return JSON.toJSONString(responseData);
    }

    @PostMapping(value = "/floworderchange")
    @AutoDocMethod(value = "订单变更", description = "订单变更", response = String.class)
    public String updateFlowOrder(UpdateTransOrderReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  updateFlowOrder request param is [{}]", reqVo);
        ResponseData responseData = ResponseData.success();
        if (bindingResult.hasErrors()) {
            responseData = validate(bindingResult);
        }
        int count = 0;
        try {
            count = orderProcessService.updateFlowOrder(reqVo);
            logger.info("***PRO*** updateFlowOrder 返回count is [{}]", count);
            if (count == NEG_ONE) {
                responseData = ResponseData.createErrorCodeResponse(FieldErrorCode.ORDER_NOT_EXIST.getCode(), FieldErrorCode.ORDER_NOT_EXIST.getText());
            }
            if (count == NEG_ZERO) {
                responseData = ResponseData.createErrorCodeResponse(FieldErrorCode.TACK_BACK_ORDER_NOT_EXIST.getCode(), FieldErrorCode.TACK_BACK_ORDER_NOT_EXIST.getText());
            }
        } catch (Exception e) {
            logger.error("***RESP*** updateFlowOrder error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]订单变更 异常 {}", e);
            responseData = ResponseData.error();
        }

        return JSON.toJSONString(responseData);
    }
}

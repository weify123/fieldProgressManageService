package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.ImportFileReqVo;
import com.autoyol.field.progress.manage.request.city.CityLevelSelectReqVo;
import com.autoyol.field.progress.manage.request.city.CityReqVo;
import com.autoyol.field.progress.manage.request.city.OrderUpdateReqVo;
import com.autoyol.field.progress.manage.response.city.CityLevelPageRespVo;
import com.autoyol.field.progress.manage.response.city.DownLoadOrderTemplateRespVo;
import com.autoyol.field.progress.manage.response.city.OrderPageRespVo;
import com.autoyol.field.progress.manage.response.city.OrderSetVo;
import com.autoyol.field.progress.manage.service.OrderSetService;
import com.autoyol.field.progress.manage.util.poi.ExcelUtil;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_ONE;
import static com.autoyol.field.progress.manage.cache.CacheConstraint.NEG_ONE;

@RestController
@RequestMapping("/console/order")
@AutoDocVersion(version = "订单高级用户设定")
public class OrderSetController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    OrderSetService orderSetService;

    @Value("${order.template.name:订单高级用户设定导入模板}")
    private String excelTemplateName;

    @Value("${order.export.name:订单高级用户设定}")
    private String excelName;

    @RequestMapping(value = "/findOrderByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询订单设定列表", description = "根据城市名[模糊查询]】订单设定列表信息", response = OrderPageRespVo.class)
    public ResponseData<OrderPageRespVo> findOrderByCond(CityReqVo reqVo) {
        logger.info("***REQ***  findOrderByCond request param is [{}]", reqVo);
        try {
            List<OrderSetVo> respVos = orderSetService.findOrderByCond(reqVo.getCityId());
            OrderPageRespVo pageRespVo = new OrderPageRespVo();
            setPage(pageRespVo, reqVo, respVos);
            pageRespVo.setOrderList(respVos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findOrderByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索订单设定列表异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectOrderByCityId", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询订单设定信息", description = "查询订单设定信息", response = OrderSetVo.class)
    public ResponseData<OrderSetVo> selectOrderByCityId(@Validated CityLevelSelectReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectOrderByCityId request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            OrderSetVo respVos = orderSetService.selectOrderByCityId(reqVo.getCityId());
            logger.info("selectOrderByCityId 查询返回 {}", respVos);
            return ResponseData.success(respVos);
        } catch (Exception e) {
            logger.error("***RESP*** findOrderByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询订单设定信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/addOrUpdateOrder", method = RequestMethod.POST)
    @AutoDocMethod(value = "新增/修改订单设定列表", description = "新增/修改订单设定列表", response = CityLevelPageRespVo.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> addOrUpdateOrder(@Validated @RequestBody OrderUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  addOrUpdateOrder request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = orderSetService.addOrUpdateOrder(reqVo);
            logger.info("addOrUpdateOrder 返回 {}", count);
            if (count == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.ORDER_IS_EXIST);
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** addOrUpdateOrder error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]订单设定异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/downloadImportTemplate", method = RequestMethod.GET)
    @AutoDocMethod(value = "下载导入模板", description = "下载导入模板", response = String.class)
    public ResponseData<String> downloadImportTemplate(HttpServletResponse response) {
        logger.info("***REQ***  downloadOrderTemplate request ");
        try {
            ExcelUtil<DownLoadOrderTemplateRespVo> excelUtil = new ExcelUtil(excelTemplateName);
            DownLoadOrderTemplateRespVo templateRespVo = new DownLoadOrderTemplateRespVo();
            templateRespVo.setCity("上海");
            templateRespVo.setRent("600");
            excelUtil.export(Collections.singletonList(templateRespVo));
            excelUtil.flushToRequest(response, excelTemplateName);
            logger.info("***RESP***  下载模板成功 ");
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** downloadOrderTemplate error:{}", e);
            Cat.logError("下载导出模板异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/exportOrder")
    @AutoDocMethod(value = "导出", description = "导出", response = String.class)
    public ResponseData<String> exportOrder(CityReqVo reqVo, HttpServletResponse response) {
        logger.info("***REQ***  exportOrder request param is [{}]", reqVo);
        try {
            List<OrderSetVo> list = orderSetService.findOrderByCond(reqVo.getCityId());
            return exportFlush(list, excelName, response);
        } catch (Exception e) {
            logger.error("***RESP*** exportOrder error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]导出订单设定异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/importOrder", method = RequestMethod.POST)
    @AutoDocMethod(value = "导入城市", description = "导入城市", response = String.class)
    @UserInfoAutoSet()
    public ResponseData<String> importOrder(@Validated ImportFileReqVo reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            return importObj(orderSetService, reqVo);
        } catch (Exception e) {
            logger.error("importOrder 导入 failed：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]importOrder 导入 {}", e);
            return ResponseData.error();
        }
    }
}

package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocParam;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.AddCarServiceItemReqVO;
import com.autoyol.field.progress.manage.request.UpdateCarServiceItemReqVO;
import com.autoyol.field.progress.manage.request.car.QueryCarServiceItemReqVo;
import com.autoyol.field.progress.manage.response.QueryCarServiceItemExcelVO;
import com.autoyol.field.progress.manage.response.QueryCarServiceItemRespListVO;
import com.autoyol.field.progress.manage.response.QueryCarServiceItemRespVO;
import com.autoyol.field.progress.manage.service.CarServiceItemService;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_ONE;
import static com.autoyol.field.progress.manage.cache.CacheConstraint.NEG_ONE;

@RestController
@RequestMapping("/console/carServiceItem")
@AutoDocVersion(version = "车辆服务项目")
public class CarServiceItemController extends BaseController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CarServiceItemService carServiceItemService;

    @Value("${car.service.export.name:车辆服务项目数据}")
    private String excelName;

    @RequestMapping(value = "/addCarServiceItem", method = RequestMethod.POST)
    @AutoDocMethod(value = "创建车辆服务项目", description = "创建车辆服务项目接口", response = ResponseData.class)
    @UserInfoAutoSet()
    public ResponseData addCarServiceItem(@Validated @RequestBody AddCarServiceItemReqVO serviceItemReqVO, BindingResult bindingResult) {
        log.info("添加车辆服务项目的请求参数：{}", serviceItemReqVO);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(serviceItemReqVO.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int num = carServiceItemService.addCarServiceItem(serviceItemReqVO);
            if (num == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.CAR_PRO_NAME_EXIST);
            }
            return ResponseData.success(num);
        } catch (Exception e) {
            log.info("添加车辆服务项目异常error：{}, param [{}]", e, serviceItemReqVO);
            Cat.logError("param [" + serviceItemReqVO + "]添加车辆服务项目异常{}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/queryCarServiceItems", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询车辆服务项目", description = "查询车辆服务项目接口", response = QueryCarServiceItemRespListVO.class)
    public ResponseData<QueryCarServiceItemRespListVO> queryCarServiceItems(QueryCarServiceItemReqVo reqVo) {
        log.info("查询车辆服务项目的请求参数：{}", reqVo);
        try {
            List<QueryCarServiceItemRespVO> items = carServiceItemService.queryCarServiceItemsByPage(reqVo);
            QueryCarServiceItemRespListVO respList = new QueryCarServiceItemRespListVO();
            setPage(respList, reqVo, items);
            respList.setList(items.stream()
                    .skip((respList.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(respList);
        } catch (Exception e) {
            log.info("查询车辆服务项目异常error：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询车辆服务项目异常{}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/queryCarServiceItemById", method = RequestMethod.GET)
    @AutoDocMethod(value = "根据id查询车辆服务项目", description = "根据id查询车辆服务项目接口", response = QueryCarServiceItemRespVO.class)
    public ResponseData<QueryCarServiceItemRespVO> queryCarServiceItemById(@AutoDocParam(name = "serviceId", description = "主键serviceId") @RequestParam("serviceId") Integer serviceId) {
        log.info("根据id查询车辆服务项目的请求参数id：{}", serviceId);
        try {
            if (serviceId == null) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), "主键id不能为空！！");
            }
            QueryCarServiceItemRespVO queryCarServiceItemRespVO = carServiceItemService.queryCarServiceItemById(serviceId);
            return ResponseData.success(queryCarServiceItemRespVO);
        } catch (Exception e) {
            log.info("根据id查询车辆服务项目异常error：{}, param [{}]", e, serviceId);
            Cat.logError("param [" + serviceId + "]根据id查询车辆服务项目异常{}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateCarServiceItem", method = RequestMethod.POST)
    @AutoDocMethod(value = "更改车辆服务项目", description = "更改车辆服务项目接口", response = ResponseData.class)
    @UserInfoAutoSet()
    public ResponseData updateCarServiceItem(@Validated @RequestBody UpdateCarServiceItemReqVO updateCarServiceItemReqVO, BindingResult bindingResult) {
        log.info("更改车辆服务项目的请求参数：{}", updateCarServiceItemReqVO);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(updateCarServiceItemReqVO.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int num = carServiceItemService.updateCarServiceItem(updateCarServiceItemReqVO);
            if (num == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.CAR_PRO_NAME_EXIST);
            }
            return ResponseData.success(num);
        } catch (Exception e) {
            log.info("更改车辆服务项目异常error：{}, param [{}]", e, updateCarServiceItemReqVO);
            Cat.logError("param [" + updateCarServiceItemReqVO + "]更改车辆服务项目异常{}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/exportCarServiceItems", method = RequestMethod.GET)
    @AutoDocMethod(value = "导出车辆服务项目", description = "导出车辆服务项目接口")
    public ResponseData<String> exportCarServiceItems(QueryCarServiceItemReqVo queryCarServiceItemReqVO, HttpServletResponse response) {
        log.info("导出车辆服务项目的请求参数：{}", queryCarServiceItemReqVO);
        try {
            List<QueryCarServiceItemExcelVO> list = carServiceItemService.exportCarServiceItems(queryCarServiceItemReqVO);
            return exportFlush(list, excelName, response);
        } catch (Exception e) {
            log.info("导出车辆服务项目异常error：{}, param [{}]", e, queryCarServiceItemReqVO);
            Cat.logError("param [" + queryCarServiceItemReqVO + "]导出车辆服务项目异常{}", e);
            return ResponseData.error();
        }
    }

}

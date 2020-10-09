package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.location.LocatePageReqVo;
import com.autoyol.field.progress.manage.request.location.LocateSelectReqVo;
import com.autoyol.field.progress.manage.response.location.LocateSelectRespVo;
import com.autoyol.field.progress.manage.response.location.LocateVo;
import com.autoyol.field.progress.manage.response.location.LocationPageRespVo;
import com.autoyol.field.progress.manage.service.PersonLocateService;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_ONE;

@RestController
@RequestMapping("/console/locate")
@AutoDocVersion(version = "外勤app用户定位信息")
public class PersonLocateController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PersonLocateService personLocateService;

    @RequestMapping(value = "/findLocateByPage", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页搜索", description = "分页条件查询用户定位状态列表", response = LocationPageRespVo.class)
    public ResponseData<LocationPageRespVo> findLocateByPage(@Validated LocatePageReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  findLocateByPage request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        if (Objects.nonNull(reqVo.getSortKey())) {
            if (Objects.isNull(reqVo.getLongitude()) || Objects.isNull(reqVo.getLatitude())) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.ADDR_IS_EMPTY);
            }
        }

        try {
            List<LocateVo> respVos = personLocateService.findLocateByPage(reqVo);
            logger.info("***RESP*** 返回respVos is [{}]", respVos);
            LocationPageRespVo pageRespVo = new LocationPageRespVo();
            setPage(pageRespVo, reqVo, respVos);
            pageRespVo.setLocateVos(respVos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findLocateByPage error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectOrderByUser", method = RequestMethod.GET)
    @AutoDocMethod(value = "用户订单信息查询", description = "用户订单信息查询", response = LocateSelectRespVo.class)
    public ResponseData<LocateSelectRespVo> selectOrderByUser(@Validated LocateSelectReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectOrderByUser request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            return ResponseData.success(personLocateService.selectOrderByUser(reqVo.getUserId()));
        } catch (Exception e) {
            logger.error("***RESP*** selectOrderByUser error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]用户订单信息查询 异常 {}", e);
            return ResponseData.error();
        }
    }
}

package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.constraint.FieldErrorCode;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.entity.CityLevelEntity;
import com.autoyol.field.progress.manage.entity.EmployCompanyEntity;
import com.autoyol.field.progress.manage.entity.SupplierAccountEntity;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.request.tackback.*;
import com.autoyol.field.progress.manage.response.tackback.PickBackFeeRespVo;
import com.autoyol.field.progress.manage.response.tackback.PickBackSelectRespVo;
import com.autoyol.field.progress.manage.response.tackback.TackBackPageRespVo;
import com.autoyol.field.progress.manage.response.tackback.TackBackPersonRespVo;
import com.autoyol.field.progress.manage.response.trans.TransOrderInfoRespVo;
import com.autoyol.field.progress.manage.response.trans.TransUserInfoRespVo;
import com.autoyol.field.progress.manage.response.trans.TransVehicleInfoRespVo;
import com.autoyol.field.progress.manage.service.CommonService;
import com.autoyol.field.progress.manage.service.PickDeliverService;
import com.autoyol.field.progress.manage.service.SupplierAccountService;
import com.dianping.cat.Cat;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.parse;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@RestController
@RequestMapping("/console/take/back")
@AutoDocVersion(version = "取还车订单菜单")
public class PickDeliverController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PickDeliverService pickDeliverService;

    @Resource
    SupplierAccountService supplierAccountService;

    @Resource
    CommonService commonService;

    @Resource
    CityCache cityCache;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    @RequestMapping(value = "/findObjByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页搜索", description = "分页条件取还车订单列表", response = TackBackPageRespVo.class)
    public ResponseData<TackBackPageRespVo> findObjByCond(@Validated TackBackPageReqVo reqVo, BindingResult bindingResult, HttpServletRequest request) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  findObjByCond request param is [{}], newUserId={}", reqVo, newUserId);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isNotEmpty(reqVo.getPickTimeMonthDay())) {
            try {
                parse(reqVo.getPickTimeYear() + reqVo.getPickTimeMonthDay(),
                        DATE_YYYY_MM_DD_CONTACT);
            } catch (Exception e) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.DATE_FORMAT_ERROR);
            }
        }

        if (supplierCond(reqVo, newUserId)) {
            return ResponseData.success();
        }

        try {
            TackBackPageRespVo respVos = pickDeliverService.findObjByCond(reqVo, newUserId);
            logger.info("***RESP*** 返回pageInfo is [{}]", respVos);
            return ResponseData.success(respVos);
        } catch (Exception e) {
            logger.error("***RESP*** findObjByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索异常 {}", e);
            return ResponseData.error();
        }
    }

    private boolean supplierCond(@Validated TackBackPageReqVo reqVo, String newUserId) {
        if (StringUtils.isNotEmpty(newUserId)) {
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (Objects.nonNull(accountEntity)) {
                List<Integer> manageCity = getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt);
                if (CollectionUtils.isEmpty(manageCity)) {
                    return true;
                }
                List<Integer> reqCityList = Optional.ofNullable(reqVo.getCityNameStr()).map(reqCity -> {
                    return getList(reqCity, SPLIT_COMMA, String::valueOf).stream().map(this::getCityName)
                            .filter(Objects::nonNull).collect(Collectors.toList());
                }).orElse(null);

                reqVo.setCityNameStr(Optional.ofNullable(reqCityList)
                        .flatMap(reqList -> reqList.stream()
                                .filter(city -> hitListSearch(manageCity, city, List::contains))
                                .map(this::getCityName)
                                .reduce((x, y) -> x + SPLIT_COMMA + y))
                        .orElse(manageCity.stream().map(this::getCityName).reduce((x, y) -> x + SPLIT_COMMA + y)
                                .orElse(null)));
                return StringUtils.isEmpty(reqVo.getCityNameStr());
            }
        }
        return false;
    }

    private Integer getCityName(String req) {
        try {
            return cityCache.getCityIdByCityNameFromCache(req);
        } catch (Exception e) {
            logger.error("***RESP*** 根据城市名查询城市id error:{}, req [{}]", e, req);
            Cat.logError("req [" + req + "]根据城市名查询城市id异常 {}", e);
            return null;
        }
    }

    private String getCityName(Integer cityId) {
        try {
            CityLevelEntity entity = cityCache.getCityByIdFromCache(cityId);
            if (Objects.isNull(entity)) {
                return null;
            }
            return entity.getCity();
        } catch (Exception e) {
            logger.error("***RESP*** 根据城市id查询城市名 error:{}, cityId [{}]", e, cityId);
            Cat.logError("cityId [" + cityId + "]根据城市id查询城市名异常 {}", e);
            return null;
        }
    }

    @RequestMapping(value = "/oprOrder", method = RequestMethod.POST)
    @AutoDocMethod(value = "取消/回退/拒单", description = "取消/回退/拒单", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> oprOrderByTackBackOrderNo(@Validated @RequestBody OprTackBackOrderReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  oprOrderByTackBackOrderNo request param is [{}]", reqVo);
        ResponseData<Integer> x = validFieldAndUser(reqVo, bindingResult);
        if (Objects.nonNull(x)) {
            return x;
        }
        try {
            parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
        } catch (Exception e) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.DATE_FORMAT_ERROR);
        }

        SupplierAccountEntity accountEntity = supplierAccountService.selectByUserId(String.valueOf(reqVo.getHandleUserNo()));
        if (Objects.nonNull(accountEntity) && reqVo.getScheduleStatusKey() != INT_FIVE) {
            return ResponseData.createErrorCodeResponse(FieldErrorCode.OPERATE_NO_PERMIT.getCode(),
                    FieldErrorCode.OPERATE_NO_PERMIT.getText());
        }
        if (Objects.isNull(accountEntity) && reqVo.getScheduleStatusKey() != INT_TOW && reqVo.getScheduleStatusKey() != INT_SEVENTEEN) {
            return ResponseData.createErrorCodeResponse(FieldErrorCode.OPERATE_NO_PERMIT.getCode(),
                    FieldErrorCode.OPERATE_NO_PERMIT.getText());
        }

        try {
            pickDeliverService.updateTackBackOrderScheduleStatus(reqVo);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** oprOrderByTackBackOrderNo TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** oprOrderByTackBackOrderNo error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]取消/回退/拒单 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/orderReduce", method = RequestMethod.POST)
    @AutoDocMethod(value = "订单生成", description = "订单生成", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> orderReduce(@Validated @RequestBody BaseTackBackReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  orderReduce request param is [{}]", reqVo);
        ResponseData<Integer> x = validFieldAndUser(reqVo, bindingResult);
        if (Objects.nonNull(x)) {
            return x;
        }
        try {
            parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
        } catch (Exception e) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.DATE_FORMAT_ERROR);
        }

        SupplierAccountEntity accountEntity = supplierAccountService.selectByUserId(String.valueOf(reqVo.getHandleUserNo()));
        if (Objects.nonNull(accountEntity)) {
            return ResponseData.createErrorCodeResponse(FieldErrorCode.OPERATE_NO_PERMIT.getCode(),
                    FieldErrorCode.OPERATE_NO_PERMIT.getText());
        }

        try {
            pickDeliverService.orderReduce(reqVo);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** orderReduce TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** orderReduce error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]订单生成 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/batch/dispatchSupplier", method = RequestMethod.POST)
    @AutoDocMethod(value = "批量分配供应商", description = "批量分配供应商", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> batchDispatchSupplier(@Validated @RequestBody TackBackBatchDispatchSupplierReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  batchDispatchSupplier request param is [{}]", reqVo);
        ResponseData<Integer> x = validFieldAndUser(reqVo, bindingResult);
        if (Objects.nonNull(x)) {
            return x;
        }
        try {
            parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
        } catch (Exception e) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.DATE_FORMAT_ERROR);
        }

        SupplierAccountEntity accountEntity = supplierAccountService.selectByUserId(String.valueOf(reqVo.getHandleUserNo()));
        if (Objects.nonNull(accountEntity)) {
            return ResponseData.createErrorCodeResponse(FieldErrorCode.OPERATE_NO_PERMIT.getCode(),
                    FieldErrorCode.OPERATE_NO_PERMIT.getText());
        }

        try {
            pickDeliverService.batchDispatchSupplier(reqVo);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** batchDispatchSupplier TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** batchDispatchSupplier error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]批量分配供应商 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单个取送订单", description = "查询单个取送订单", response = PickBackSelectRespVo.class)
    public ResponseData<PickBackSelectRespVo> selectByCond(@Validated PickBackSelectReqVo reqVo, BindingResult bindingResult, HttpServletRequest request) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  selectByCond request param is [{}] newUserId,{}", reqVo, newUserId);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        Date pickTime;
        try {
            pickTime = parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
        } catch (Exception e) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.DATE_FORMAT_ERROR);
        }
        try {
            return ResponseData.success(pickDeliverService.selectByCond(reqVo, pickTime,
                    Optional.ofNullable(newUserId).filter(StringUtils::isNotEmpty).map(Integer::parseInt).orElse(null)));
        } catch (Exception e) {
            logger.error("***RESP*** selectByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单个取送订单 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectTransOrderInfo", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单个交易订单信息", description = "查询单个交易订单信息", response = TransOrderInfoRespVo.class)
    public ResponseData<TransOrderInfoRespVo> selectTransOrderInfo(@Validated TransSelectOrderReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectTransOrderInfo request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            return ResponseData.success(pickDeliverService.selectTransOrderInfo(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), reqVo.getTransOrderNo()));
        } catch (Exception e) {
            logger.error("***RESP*** selectTransOrderInfo error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单个交易订单信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectTransVehicleInfo", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单个交易车辆信息", description = "查询单个交易车辆信息", response = TransVehicleInfoRespVo.class)
    public ResponseData<TransVehicleInfoRespVo> selectTransVehicleInfo(@Validated BaseTackBackReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectTransVehicleInfo request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            return ResponseData.success(pickDeliverService.selectTransVehicleInfo(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** selectTransVehicleInfo error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单个交易车辆信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectTransUserInfo", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单个交易用戶信息", description = "查询单个交易用戶信息", response = TransUserInfoRespVo.class)
    public ResponseData<TransUserInfoRespVo> selectTransUserInfo(@Validated TransSelectOrderReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectTransUserInfo request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            return ResponseData.success(pickDeliverService.selectTransUserInfo(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** selectTransUserInfo error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单个交易用戶信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectTackBackFeeInfo", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单个取送车费用信息", description = "查询单个取送车费用信息", response = PickBackFeeRespVo.class)
    public ResponseData<PickBackFeeRespVo> selectTackBackFeeInfo(@Validated BaseTackBackReqVo reqVo, BindingResult bindingResult, HttpServletRequest request) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  selectTransUserInfo request param is [{}] newUserId {}", reqVo, newUserId);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        Date pickTime;
        try {
            pickTime = parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
        } catch (Exception e) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.DATE_FORMAT_ERROR);
        }
        try {
            return ResponseData.success(pickDeliverService.selectTackBackFeeInfo(reqVo.getPickDeliverOrderNo(), reqVo.getServerTypeKey(), pickTime,
                    Optional.ofNullable(newUserId).filter(StringUtils::isNotEmpty).map(Integer::parseInt).orElse(null)));
        } catch (Exception e) {
            logger.error("***RESP*** selectTransUserInfo error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单个取送车费用信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/queryTackBackPerson", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询可派单人员", description = "查询派单人员", response = TackBackPersonRespVo.class)
    @UserInfoAutoSet
    public ResponseData<TackBackPersonRespVo> queryTackBackPerson(@Validated TackBackPersonReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  queryTackBackPerson request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (Objects.isNull(reqVo.getHandleUserNo())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        if (StringUtils.equalsIgnoreCase(dbRenYunSwitch, sqlServer_switch)) {
            EmployCompanyEntity companyEntity = pickDeliverService.getSupplierCompany(String.valueOf(reqVo.getHandleUserNo()));
            reqVo.setCompanyId(getObjOrDefault(companyEntity, EmployCompanyEntity::getId, reqVo.getCompanyId()));
        } else {
            SupplierAccountEntity accountEntity = supplierAccountService.selectByUserId(String.valueOf(reqVo.getHandleUserNo()));

            if (Objects.nonNull(accountEntity) && Objects.isNull(accountEntity.getCompanyId())) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.BELONG_COMPANY_NOT_CONFIG);
            }
            if (Objects.nonNull(accountEntity) && Objects.nonNull(accountEntity.getCompanyId())) {
                reqVo.setCompanyId(accountEntity.getCompanyId());
            }
        }

        try {
            return ResponseData.success(pickDeliverService.queryTackBackPerson(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** queryTackBackPerson error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询可派单人员 异常 {}", e);
            return ResponseData.error();
        }
    }
}

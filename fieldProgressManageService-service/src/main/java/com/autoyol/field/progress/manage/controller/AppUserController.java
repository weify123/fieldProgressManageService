package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.constraint.FieldErrorCode;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.entity.SupplierAccountEntity;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.request.user.*;
import com.autoyol.field.progress.manage.response.user.AppUserPageRespVo;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import com.autoyol.field.progress.manage.service.AppUserService;
import com.autoyol.field.progress.manage.service.CommonService;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.autoyol.field.progress.manage.util.poi.ExcelUtil;
import com.dianping.cat.Cat;
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
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;


/**
 * 实例模板controller的创建
 **/
@RestController
@RequestMapping("/console/appUser")
@AutoDocVersion(version = "外勤app用户信息管理")
public class AppUserController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AppUserService appUserService;

    @Resource
    CommonService commonService;

    @Value("${app.user.export.name:外勤APP用户信息}")
    private String excelName;

    @RequestMapping(value = "/findUserByPage", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页搜索", description = "分页条件查询用户列表", response = AppUserPageRespVo.class)
    public ResponseData<AppUserPageRespVo> findUserByPage(AppUserQueryReqVo userReqVO, HttpServletRequest request) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  findUserByPage request param is [{}], newUserId = {}", userReqVO, newUserId);
        if (StringUtils.isNotEmpty(newUserId)) {
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (resetSupplierCond(userReqVO, accountEntity)) {
                return ResponseData.success(new AppUserPageRespVo());
            }
        }
        try {
            AppUserPageRespVo resp = appUserService.findUserByPage(userReqVO);
            logger.info("***RESP*** 返回pageInfo is [{}]", resp);
            return ResponseData.success(resp);
        } catch (Exception e) {
            logger.error("***RESP*** findUserByPage error:{}, param [{}]", e, userReqVO);
            Cat.logError("参数[" + userReqVO + "]条件搜索异常 {}", e);
            return ResponseData.error();
        }

    }

    @RequestMapping(value = "/findUserById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单条记录", description = "根据用户id查询单条用户", response = AppUserVo.class)
    public ResponseData<AppUserVo> findUserById(@Validated BaseAppUserReqVo reqVo, BindingResult bindingResult, HttpServletRequest request) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  findUserById request param is [{}] newUserId={}", reqVo, newUserId);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            AppUserVo respVo = appUserService.findUserById(reqVo.getUserId(), newUserId);
            logger.info("***RESP*** 返回respVo is [{}]", respVo);
            return ResponseData.success(respVo);
        } catch (Exception e) {
            logger.error("***RESP*** findUserById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]appUser单条查询异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateByUserId", method = RequestMethod.POST)
    @AutoDocMethod(value = "修改单个用户信息", description = "根据用户id 修改用戶信息 返回-1设置失败,-2为姓名已存在，-3为手机已存在, 返回-4用户已注销", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> updateByUserId(@Validated @RequestBody AppUserUpdateReqVo updateReqVo, BindingResult bindingResult) {
        logger.info("***REQ*** updateByUserId request param is [{}]", updateReqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(updateReqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            appUserService.updateByUserId(updateReqVo);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** updateByUserId TackBackException error:{}, param [{}]", e, updateReqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** updateByUserId error:{}, param [{}]", e, updateReqVo);
            Cat.logError("param [" + updateReqVo + "]修改用戶信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/addAppUser", method = RequestMethod.POST)
    @AutoDocMethod(value = "新增单条用户信息", description = "新增单条用户信息 返回-1设置失败,-2为姓名已存在，-3为手机已存在", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> addAppUser(@Validated @RequestBody AppUserAddReqVo userAddReqVo, BindingResult bindingResult) {
        logger.info("***REQ*** addAppUser request param is [{}]", userAddReqVo);

        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(userAddReqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            appUserService.addAppUser(userAddReqVo);
            return ResponseData.success();
        } catch (TackBackException e) {
            logger.error("***RESP*** addAppUser TackBackException error:{}, param [{}]", e, userAddReqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** addAppUser error:{}, param [{}]", e, userAddReqVo);
            Cat.logError("param [" + userAddReqVo + "]新增用户信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/setUserPassWord", method = RequestMethod.POST)
    @AutoDocMethod(value = "设置密码", description = "设置初始密码 返回-1设置失败, 返回-4用户已注销", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> setUserPassWord(@Validated @RequestBody AppUserSetPassWordReqVo passWordReqVo, BindingResult bindingResult) {
        logger.info("***REQ*** setUserPassWord request param is [{}]", passWordReqVo);

        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(passWordReqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = appUserService.setUserPassWord(passWordReqVo);
            logger.info("***RESP*** setUserPassWord count is [{}]", count);
            if (count == NEG_ZERO) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.NOT_ALLOWED_SET_PASS);
            }
            if (count == NEG_FOUR) {
                return ResponseData.createErrorCodeResponse(FieldErrorCode.USER_STATUS_DISABLE.getCode(), FieldErrorCode.USER_STATUS_DISABLE.getText());
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** setUserPassWord error:{}, param [{}]", e, passWordReqVo);
            Cat.logError("param [" + passWordReqVo + "]设置初始密码异常 [{}]", e);
            return ResponseData.error();
        }
    }


    @RequestMapping(value = "/exportAppUser")
    @AutoDocMethod(value = "导出", description = "导出", response = Integer.class)
    public ResponseData<String> exportAppUser(AppUserQueryReqVo reqVO, HttpServletRequest request, HttpServletResponse response) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  exportAppUser request param is [{}], newUserId = {}", reqVO, newUserId);
        if (StringUtils.isNotEmpty(newUserId)) {
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (resetSupplierCond(reqVO, accountEntity)) {
                return ResponseData.success(ErrorCode.SUCCESS.getText());
            }
        }
        try {
            AppUserPageRespVo respVo = appUserService.findUserByPage(reqVO);
            if (CollectionUtils.isEmpty(respVo.getUserList())) {
                return ResponseData.createErrorCodeResponse(FieldErrorCode.EXPORT_DATA_EMPTY.getCode(), FieldErrorCode.EXPORT_DATA_EMPTY.getText());
            }

            ExcelUtil<AppUserVo> excelUtil = new ExcelUtil<>(excelName);
            excelUtil.export(respVo.getUserList().stream().peek(o -> {
                o.setCreateTimeStr(LocalDateUtil.dateToStringFormat(o.getCreateTime(), CacheConstraint.DATE_FULL));
                o.setUpdateTimeStr(Optional.ofNullable(o.getUpdateTime()).map(updateDate -> LocalDateUtil.dateToStringFormat(updateDate, CacheConstraint.DATE_FULL)).orElse(STRING_EMPTY));
            }).collect(Collectors.toList()));
            excelUtil.flushToRequest(response, excelName);
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** findUserByPage error:{}, param [{}]", e, reqVO);
            Cat.logError("param [" + reqVO + "]条件搜索异常 {}", e);
            return ResponseData.error();
        }
    }
}

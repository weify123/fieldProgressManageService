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
import com.autoyol.field.progress.manage.request.ImportFileReqVo;
import com.autoyol.field.progress.manage.request.user.BaseUserInfoReqVo;
import com.autoyol.field.progress.manage.request.user.UserInfoSearchReqVo;
import com.autoyol.field.progress.manage.request.user.UserInfoUpdateReqVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoPageRespVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoVo;
import com.autoyol.field.progress.manage.response.userinfo.ExportUserInfoRespVo;
import com.autoyol.field.progress.manage.service.AppUserInfoService;
import com.autoyol.field.progress.manage.service.CommonService;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.autoyol.field.progress.manage.util.poi.ExcelUtil;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;


/**
 * 实例模板controller的创建
 **/
@RestController
@RequestMapping("/console/appUserInfo")
@AutoDocVersion(version = "外勤人员管理")
public class AppUserInfoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AppUserInfoService appUserInfoService;

    @Resource
    CommonService commonService;

    @Value("${user.info.export.name:外勤人员管理}")
    private String excelName;

    @RequestMapping(value = "/findUserInfoByPage", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页搜索", description = "分页条件查询用户详细列表", response = AppUserInfoPageRespVo.class)
    public ResponseData<AppUserInfoPageRespVo> findUserInfoByPage(HttpServletRequest request, UserInfoSearchReqVo reqVo) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  findUserInfoByPage request param is [{}] newUserId = {}", reqVo, newUserId);
        if (StringUtils.isNotEmpty(newUserId)){
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (resetSupplierCond(reqVo, accountEntity)) {
                return ResponseData.success(new AppUserInfoPageRespVo());
            }
        }

        try {
            AppUserInfoPageRespVo respVos = appUserInfoService.findUserByPage(reqVo);
            logger.info("***PRO*** findUserInfoByPage 分页搜索 param is [{}] respVos {}",reqVo, respVos);
            return ResponseData.success(respVos);
        } catch (Exception e) {
            logger.error("***RESP*** findUserInfoByPage error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/findUserInfoById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询用户信息单条记录", description = "根据id查询单条用户", response = AppUserInfoVo.class)
    @UserInfoAutoSet()
    public ResponseData<AppUserInfoVo> findUserInfoById(@Validated BaseUserInfoReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  findUserInfoById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            AppUserInfoVo respVo = appUserInfoService.findUserInfoById(reqVo.getUserId(), String.valueOf(reqVo.getHandleUserNo()));
            logger.info("***RESP*** 返回respVo is [{}]", respVo);
            return ResponseData.success(respVo);
        } catch (Exception e) {
            logger.error("***RESP*** findUserInfoById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询用户信息单条记录异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateUserInfoById", method = RequestMethod.POST)
    @AutoDocMethod(value = "修改用户基本信息", description = "根据id 修改用户基本信息", response = AppUserInfoVo.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> updateUserInfoById(@Validated @RequestBody UserInfoUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  updateUserInfoById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            appUserInfoService.updateUserInfoById(reqVo);
            return ResponseData.success();
        } catch (TackBackException e){
            logger.error("***RESP*** updateUserInfoById TackBackException error:{}, param [{}]", e, reqVo);
            return ResponseData.createErrorCodeResponse(e.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("***RESP*** updateUserInfoById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]修改用户基本信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/exportUserInfo")
    @AutoDocMethod(value = "导出", description = "导出", response = String.class)
    public ResponseData<String> exportUserInfo(UserInfoSearchReqVo reqVo, HttpServletRequest request, HttpServletResponse response) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  exportAppUser request param is [{}]，newUserId={}", reqVo, newUserId);
        if (StringUtils.isNotEmpty(newUserId)){
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (resetSupplierCond(reqVo, accountEntity)) {
                return ResponseData.success(ErrorCode.SUCCESS.getText());
            }
        }
        try {
            AppUserInfoPageRespVo respVos = appUserInfoService.findUserByPage(reqVo);
            if (CollectionUtils.isEmpty(respVos.getUserInfoList())) {
                return ResponseData.createErrorCodeResponse(FieldErrorCode.EXPORT_DATA_EMPTY.getCode(), FieldErrorCode.EXPORT_DATA_EMPTY.getText());
            }

            ExcelUtil<ExportUserInfoRespVo> excelUtil = new ExcelUtil<>(excelName);
            excelUtil.export(respVos.getUserInfoList().stream().map(o -> {
                ExportUserInfoRespVo export = new ExportUserInfoRespVo();
                BeanUtils.copyProperties(o, export);
                export.setEmployment(o.getEmploymentVal());
                export.setStar(o.getStarVal());
                export.setStation(o.getStationVal());
                export.setDistribute(o.getDistributeVal());
                export.setUpdateTimeStr(Optional.ofNullable(o.getUpdateTime()).map(updateDate -> LocalDateUtil.dateToStringFormat(updateDate, CacheConstraint.DATE_FULL)).orElse(STRING_EMPTY));
                return export;
            }).collect(Collectors.toList()));
            excelUtil.flushToRequest(response, excelName);
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** exportAppUser error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]导出异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/importUserInfo", method = RequestMethod.POST)
    @AutoDocMethod(value = "导入", description = "导入", response = String.class)
    @UserInfoAutoSet()
    public ResponseData<String> importUserInfo(@Validated ImportFileReqVo reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = appUserInfoService.importObj(
                    getWorkBook(reqVo.getAttachmentFile().getOriginalFilename(), reqVo.getAttachmentFile().getInputStream()),
                    reqVo.getHandleUser() + SPLIT_COMMA + reqVo.getHandleUserNo());
            ResponseData<String> x = validImport(count);
            if (Objects.nonNull(x)) {
                return x;
            }
            if (count == NEG_FIVE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.EXCEL_CONTENT_CONTACT_MOBILE_MUL);
            }
            if (count == NEG_SIX) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.EXCEL_CONTENT_ID_NO_MUL);
            }
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("importUserInfo 导入 error：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]importUserInfo 导入 {}", e);
            return ResponseData.error();
        }
    }

}

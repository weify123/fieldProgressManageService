package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.request.BaseSelectReqVo;
import com.autoyol.field.progress.manage.request.ImportFileReqVo;
import com.autoyol.field.progress.manage.request.supplier.SupplierCompanyReqVo;
import com.autoyol.field.progress.manage.request.supplier.SupplierPageReqVo;
import com.autoyol.field.progress.manage.request.supplier.SupplierUpdateVo;
import com.autoyol.field.progress.manage.response.company.CompanyCategoryRespVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierCompanyRespVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierPagePageRespVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierRoleVo;
import com.autoyol.field.progress.manage.response.supplier.SupplierVo;
import com.autoyol.field.progress.manage.service.SupplierAccountService;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_ONE;
import static com.autoyol.field.progress.manage.cache.CacheConstraint.NEG_ONE;

@RestController
@RequestMapping("/console/supplierAccount")
@AutoDocVersion(version = "供应商账号管理")
public class SupplierAccountController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    SupplierAccountService supplierAccountService;

    @Value("${supplier.export.name:供应商账号管理}")
    private String excelName;

    @RequestMapping(value = "/findSupplierAccountByPage", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页搜索", description = "分页条件查询供应商账号管理列表", response = SupplierPagePageRespVo.class)
    public ResponseData<SupplierPagePageRespVo> findSupplierAccountByPage(SupplierPageReqVo reqVo) {
        logger.info("***REQ***  findSupplierAccountByPage request param is [{}]", reqVo);

        try {
            List<SupplierVo> respVos = supplierAccountService.findSupplierAccountByPage(reqVo);
            logger.info("***RESP*** 返回respVos is [{}]", respVos);
            SupplierPagePageRespVo pageRespVo = new SupplierPagePageRespVo();
            setPage(pageRespVo, reqVo, respVos);
            pageRespVo.setSupplierVos(respVos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findSupplierAccountByPage error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/export")
    @AutoDocMethod(value = "导出", description = "导出", response = String.class)
    public ResponseData<String> export(SupplierPageReqVo reqVo, HttpServletResponse response) {
        logger.info("***REQ***  export request param is [{}]", reqVo);
        try {
            List<SupplierVo> list = supplierAccountService.findSupplierAccountByPage(reqVo);
            return exportFlush(list, excelName, response);
        } catch (Exception e) {
            logger.error("***RESP*** export error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]导出异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单条", description = "查询单条", response = SupplierVo.class)
    public ResponseData<SupplierVo> selectById(@Validated BaseSelectReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectById 查询单条 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            SupplierVo supplierVo = supplierAccountService.selectById(reqVo.getId());
            logger.info("***RESP*** 查询单条 supplierVo={}", supplierVo);
            return ResponseData.success(supplierVo);
        } catch (Exception e) {
            logger.error("***RESP*** selectById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单条 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/querySupplierByName", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询登录用户供应商信息", description = "查询登录用户供应商信息", response = SupplierRoleVo.class)
    @UserInfoAutoSet()
    public ResponseData<SupplierRoleVo> querySupplierByName(BaseRequest reqVo) {
        logger.info("***REQ***  selectById 查询登录用户供应商信息 param {}", reqVo);
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.ALREADY_LOGOUT);
        }
        try {
            SupplierRoleVo role = supplierAccountService.querySupplierByName(reqVo.getHandleUser());
            logger.info("***RESP*** 查询登录用户供应商信息 role={}", role);
            if (role.getRoleId().equals(NEG_ONE)) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.BELONG_COMPANY_NOT_CONFIG);
            }
            return ResponseData.success(role);
        } catch (Exception e) {
            logger.error("***RESP*** selectById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询登录用户供应商信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    @AutoDocMethod(value = "修改单条", description = "修改单条", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> updateById(@Validated @RequestBody SupplierUpdateVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  updateById 修改单条 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = supplierAccountService.updateById(reqVo);
            logger.info("***RESP*** 修改单条 count={}", count);
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** updateById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]修改单条 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @AutoDocMethod(value = "导入", description = "导入", response = String.class)
    @UserInfoAutoSet()
    public ResponseData<String> importExcel(@Validated ImportFileReqVo reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            return importObj(supplierAccountService, reqVo);
        } catch (Exception e) {
            logger.error("importExcel 导入 failed：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]importExcel 导入 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/queryCompany", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询公司列表(除外包)", description = "查询公司列表(除外包)", response = CompanyCategoryRespVo.class)
    public ResponseData<CompanyCategoryRespVo> queryCompany() {
        logger.info("***REQ***  queryCompany 查询公司列表(除外包)");
        try {
            CompanyCategoryRespVo respVo = supplierAccountService.queryCompany();
            logger.info("***RESP*** 查询公司列表(除外包) respVo={}", respVo);
            return ResponseData.success(respVo);
        } catch (Exception e) {
            logger.error("***RESP*** queryCompany error:{}", e);
            Cat.logError("查询公司列表(除外包) 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/querySupplierCompany", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询供应商公司列表", description = "查询供应商公司列表", response = SupplierCompanyRespVo.class)
    @UserInfoAutoSet
    public ResponseData<SupplierCompanyRespVo> querySupplierCompany(SupplierCompanyReqVo reqVo) {
        logger.info("***REQ***  querySupplierCompany 查询供应商公司列表 param {}", reqVo);
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.ALREADY_LOGOUT);
        }
        /*SupplierAccountEntity accountEntity = supplierAccountService.selectByUserId(String.valueOf(reqVo.getHandleUserNo()));
        if (Objects.nonNull(accountEntity))
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATE_NO_PERMIT);*/
        try {
            SupplierCompanyRespVo respVo = supplierAccountService.querySupplierCompany(reqVo.getTransOrderNo());
            logger.info("***RESP*** 查询供应商公司列表 respVo={}", respVo);
            return ResponseData.success(respVo);
        } catch (Exception e) {
            logger.error("***RESP*** querySupplierCompany error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询供应商公司列表 异常 {}", e);
            return ResponseData.error();
        }
    }

}

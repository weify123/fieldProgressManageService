package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.company.CompanyAddCategoryReqVo;
import com.autoyol.field.progress.manage.request.company.CompanyPageReqVo;
import com.autoyol.field.progress.manage.request.company.CompanyQueryReqVo;
import com.autoyol.field.progress.manage.request.company.CompanyUpdateReqVo;
import com.autoyol.field.progress.manage.response.company.CompanyCategoryRespVo;
import com.autoyol.field.progress.manage.response.company.CompanyPageRespVo;
import com.autoyol.field.progress.manage.response.company.CompanyVo;
import com.autoyol.field.progress.manage.service.CompanyService;
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

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;

@RestController
@RequestMapping("/console/company")
@AutoDocVersion(version = "公司管理操作")
public class CompanyController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CompanyService companyService;

    @Value("${company.export.name:服务公司维护表}")
    private String excelName;

    @RequestMapping(value = "/findCompanyByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询公司列表", description = "根据条件查询公司分页信息", response = CompanyPageRespVo.class)
    public ResponseData<CompanyPageRespVo> findCompanyByCond(CompanyPageReqVo reqVo) {
        logger.info("***REQ***  findCompanyByCond request param is [{}]", reqVo);
        try {
            List<CompanyVo> respVos = companyService.findCompanyByCond(reqVo);
            CompanyPageRespVo pageRespVo = new CompanyPageRespVo();
            setPage(pageRespVo, reqVo, respVos);
            pageRespVo.setCompanyList(respVos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findCompanyByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索公司列表异常 [{}]", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/queryCompanyCategory", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询公司分类列表", description = "查询公司分类列表", response = CompanyCategoryRespVo.class)
    public ResponseData<CompanyCategoryRespVo> queryCompanyCategory() {
        logger.info("***REQ***  queryCompanyCategory");
        try {
            CompanyCategoryRespVo respVos = companyService.queryCompanyCategory(null);
            logger.info("***RESP*** 返回公司分类列表 is [{}]", respVos);
            return ResponseData.success(respVos);
        } catch (Exception e) {
            logger.error("***RESP*** queryCompanyCategory error:{}", e);
            Cat.logError("查询公司分类列表异常 [{}]", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/addCompanyCategory", method = RequestMethod.POST)
    @AutoDocMethod(value = "新增公司分类", description = "新增公司分类,若返回[-1]表示二级分类名已存在", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> addCompanyCategory(@Validated @RequestBody CompanyAddCategoryReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  addCompanyCategory request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = companyService.addCompanyCategory(reqVo);
            logger.info("***RESP*** 返回新增公司分类 count is [{}]", count);
            if (count == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.SECOND_COMPANY_EXIST);
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** addCompanyCategory error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]新增公司分类异常 [{}]", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/queryCompanyById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单条公司记录", description = "查询单条公司记录", response = CompanyVo.class)
    public ResponseData<CompanyVo> queryCompanyById(@Validated CompanyQueryReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  queryCompanyById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            CompanyVo respVo = companyService.queryCompanyById(reqVo.getCompanyId());
            logger.info("***RESP*** 查询单条公司记录 respVo is [{}]", respVo);
            return ResponseData.success(respVo);
        } catch (Exception e) {
            logger.error("***RESP*** queryCompanyById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单条公司记录异常 [{}]", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateCompanyCategory", method = RequestMethod.POST)
    @AutoDocMethod(value = "修改公司分类", description = "修改公司分类,若返回[-1]表示二级分类名已存在", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> updateCompanyCategory(@Validated @RequestBody CompanyUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  updateCompanyCategory request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = companyService.updateCompanyCategory(reqVo);
            logger.info("***RESP*** 返回修改公司分类 count is [{}]", count);
            if (count == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.SECOND_COMPANY_EXIST);
            }
            if (count == NEG_TOW) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.COMPANY_USER_EXIST);
            }
            if (count == NEG_THREE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.COMPANY_SUPPLIER_EXIST);
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** updateCompanyCategory error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]修改公司分类异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/exportCompany")
    @AutoDocMethod(value = "导出", description = "导出", response = Integer.class)
    public ResponseData<String> exportCompany(CompanyPageReqVo reqVo, HttpServletResponse response) {
        logger.info("***REQ***  exportCompany request param is [{}]", reqVo);
        try {
            List<CompanyVo> list = companyService.findCompanyByCond(reqVo);
            return exportFlush(list, excelName, response);
        } catch (Exception e) {
            logger.error("***RESP*** exportCompany error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]exportCompany异常 [{}]", e);
            return ResponseData.error();
        }
    }
}

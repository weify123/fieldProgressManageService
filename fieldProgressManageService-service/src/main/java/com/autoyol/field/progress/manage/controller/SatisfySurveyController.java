package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.page.BasePage;
import com.autoyol.field.progress.manage.request.survey.QuerySingleReqVo;
import com.autoyol.field.progress.manage.request.survey.SurveyAddReqVo;
import com.autoyol.field.progress.manage.request.survey.SurveyUpdateReqVo;
import com.autoyol.field.progress.manage.request.survey.SurveyUrlReqVo;
import com.autoyol.field.progress.manage.response.survey.SurveyPageRespVo;
import com.autoyol.field.progress.manage.response.survey.SurveyRespVo;
import com.autoyol.field.progress.manage.service.SatisfyService;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_ONE;

@RestController
@RequestMapping("/console/satisfy")
@AutoDocVersion(version = "取送车服务租客满意度调查模板")
public class SatisfySurveyController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    SatisfyService satisfyService;

    @RequestMapping(value = "/findPageByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询满意度调查模板", description = "分页查询满意度调查模板", response = SurveyPageRespVo.class)
    public ResponseData<SurveyPageRespVo> findPageByCond(BasePage reqVo) {
        logger.info("***REQ***  findPageByCond 满意度分页查询 param {}", reqVo);
        try {
            List<SurveyRespVo> respVos = satisfyService.findPageByCond();
            SurveyPageRespVo pageRespVo = new SurveyPageRespVo();
            setPage(pageRespVo, reqVo, respVos);
            pageRespVo.setList(respVos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findPageByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]满意度分页查询异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @AutoDocMethod(value = "满意度单条查询", description = "满意度单条查询", response = SurveyRespVo.class)
    public ResponseData<SurveyRespVo> selectById(QuerySingleReqVo reqVo) {
        logger.info("***REQ***  selectById 满意度单条查询 param {}", reqVo);
        try {
            SurveyRespVo vo = satisfyService.selectById(reqVo.getId());
            logger.info("***REQ***  selectById 满意度单条查询 返回 {}", vo);
            return ResponseData.success(vo);
        } catch (Exception e) {
            logger.error("***RESP*** selectById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]满意度单条查询异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @AutoDocMethod(value = "满意度单条新增", description = "满意度单条新增", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> add(@Validated @RequestBody SurveyAddReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  add 满意度单条新增 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = satisfyService.add(reqVo);
            logger.info("***REQ***  add 满意度单条新增 返回 {}", count);
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** add error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]满意度单条新增异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "满意度单条修改", description = "满意度单条修改", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> update(@Validated @RequestBody SurveyUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  add 满意度单条修改 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = satisfyService.update(reqVo);
            logger.info("***REQ***  add 满意度单条修改 返回 {}", count);
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** add error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]满意度单条修改异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/getShortUrl", method = RequestMethod.POST)
    @AutoDocMethod(value = "满意度获取短连接", description = "满意度获取短连接", response = String.class)
    public ResponseData<String> getShortUrl(@Validated @RequestBody SurveyUrlReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  getShortUrl 满意度单条修改 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            return ResponseData.success(satisfyService.getShortUrl(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** getShortUrl error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]满意度获取短连接异常 {}", e);
            return ResponseData.error();
        }
    }

}

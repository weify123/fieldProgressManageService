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
import com.autoyol.field.progress.manage.request.city.CityLevelUpdateReqVo;
import com.autoyol.field.progress.manage.request.city.CityReqVo;
import com.autoyol.field.progress.manage.response.city.*;
import com.autoyol.field.progress.manage.service.CityService;
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

@RestController
@RequestMapping("/console/city")
@AutoDocVersion(version = "城市管理操作")
public class CityController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    CityService cityService;

    @Value("${city.export.name:城市等级维护}")
    private String excelName;

    @Value("${city.template.name:城市等级维护导入模板}")
    private String excelTemplateName;

    @RequestMapping(value = "/findCityByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询城市列表", description = "根据城市名[模糊查询]城市分页信息", response = CityLevelPageRespVo.class)
    public ResponseData<CityLevelPageRespVo> findCityByCond(CityReqVo reqVo) {
        logger.info("***REQ***  findUserByPage request param is [{}]", reqVo);
        try {
            List<CityLevelVo> respVos = cityService.findCityByCond(reqVo.getCityId());
            CityLevelPageRespVo pageRespVo = new CityLevelPageRespVo();
            setPage(pageRespVo, reqVo, respVos);
            pageRespVo.setCityList(respVos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findUserByPage error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索城市列表异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/findAllCity", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询所有城市列表", description = "查询导航栏城市列表信息", response = CityLevelAllRespVo.class)
    public ResponseData<CityLevelAllRespVo> findAllCity() {
        try {
            CityLevelAllRespVo respVos = cityService.findAllCity();
            logger.info("***RESP*** 查询所有城市列表 is [{}]", respVos);
            return ResponseData.success(respVos);
        } catch (Exception e) {
            logger.error("***RESP*** findAllCity error:{}", e);
            Cat.logError("查询导航栏城市列表信息 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectCityById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单条城市信息", description = "查询单条城市信息", response = CityLevelQueryRespVo.class)
    public ResponseData<CityLevelQueryRespVo> selectCityById(@Validated CityLevelSelectReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectCityById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            CityLevelQueryRespVo respVos = cityService.selectCityById(reqVo.getCityId());
            logger.info("***RESP*** 查询单条城市信息 is [{}]", respVos);
            return ResponseData.success(respVos);
        } catch (Exception e) {
            logger.error("***RESP*** findAllCity error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询导航栏城市列表信息 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateCityById", method = RequestMethod.POST)
    @AutoDocMethod(value = "更新城市等级", description = "更新城市等级", response = CityLevelAllRespVo.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> updateCityById(@Validated @RequestBody CityLevelUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  updateCityById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = cityService.updateCityById(reqVo);
            logger.info("***RESP*** 更新城市等级 count is [{}]", count);
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** updateCityById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]更新城市等级 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/exportCity")
    @AutoDocMethod(value = "导出", description = "导出", response = String.class)
    public ResponseData<String> exportCity(CityReqVo reqVo, HttpServletResponse response) {
        logger.info("***REQ***  exportCity request param is [{}]", reqVo);
        try {
            List<CityLevelVo> list = cityService.findCityByCond(reqVo.getCityId());
            return exportFlush(list, excelName, response);
        } catch (Exception e) {
            logger.error("***RESP*** exportCity error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]导出城市异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/downloadCityTemplate")
    @AutoDocMethod(value = "下载导入模板", description = "下载导入模板", response = String.class)
    public ResponseData<String> downloadCityTemplate(HttpServletResponse response) {
        logger.info("***REQ***  downloadCityTemplate request ");
        try {
            ExcelUtil<DownLoadCityTemplateRespVo> excelUtil = new ExcelUtil(excelTemplateName);
            DownLoadCityTemplateRespVo templateRespVo = new DownLoadCityTemplateRespVo();
            templateRespVo.setCity("上海");
            templateRespVo.setCityLevel(1);
            excelUtil.export(Collections.singletonList(templateRespVo));
            excelUtil.flushToRequest(response, excelTemplateName);
            logger.info("***RESP***  下载模板成功 ");
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** downloadCityTemplate error:{}", e);
            Cat.logError("下载导出模板异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/importCity", method = RequestMethod.POST)
    @AutoDocMethod(value = "导入", description = "导入", response = String.class)
    @UserInfoAutoSet()
    public ResponseData<String> importCity(@Validated ImportFileReqVo reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            return importObj(cityService, reqVo);
        } catch (Exception e) {
            logger.error("importCity 导入 failed：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]importCity 导入 {}", e);
            return ResponseData.error();
        }
    }
}

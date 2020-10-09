package com.autoyol.field.progress.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.cache.AttrLabelCache;

import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.convert.ConvertEntityToVo;
import com.autoyol.field.progress.manage.entity.AttrLabelEntity;
import com.autoyol.field.progress.manage.entity.HideFieldConfEntity;
import com.autoyol.field.progress.manage.enums.UploadEnum;
import com.autoyol.field.progress.manage.mapper.HideFieldConfMapper;
import com.autoyol.field.progress.manage.page.BasePage;
import com.autoyol.field.progress.manage.request.AttrFeeReqVo;
import com.autoyol.field.progress.manage.request.ImportLogReqVo;
import com.autoyol.field.progress.manage.request.UploadReqVo;
import com.autoyol.field.progress.manage.request.dict.DictReqVo;
import com.autoyol.field.progress.manage.response.*;
import com.autoyol.field.progress.manage.service.DictService;
import com.autoyol.field.progress.manage.service.ImportService;
import com.autoyol.field.progress.manage.service.TackBackInfoService;
import com.autoyol.field.progress.manage.service.UserInfoTmpService;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
import com.autoyol.field.progress.manage.util.http.ErrorCode;
import com.autoyol.field.progress.manage.util.http.HttpUtils;
import com.autoyol.field.progress.manage.util.http.UploadAuthVo;
import com.dianping.cat.Cat;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@RestController
@RequestMapping("/console/common")
@AutoDocVersion(version = "公共配置操作")
public class CommonController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    DictService dictService;

    @Resource
    ImportService importService;

    @Resource
    UserInfoTmpService userInfoTmpService;

    @Resource
    TackBackInfoService tackBackInfoService;

    @Resource
    AttrLabelCache attrLabelCache;

    @Resource
    HideFieldConfMapper hideFieldConfMapper;

    @Value("${oss.url}")
    private String ossUrl;

    @Value("${oss.bucket}")
    private String realBucket;

    @Value("${oss.callToken}")
    private String callToken;

    @RequestMapping(value = "/findDictByName", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询字典列表", description = "根据类型名查询字典信息", response = SysDictRespVo.class)
    public ResponseData<DictMulRespVo> findDictByName(@Validated DictReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  findDictByName request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            Map<String, List<SysDictRespVo>> respVos = dictService.findDictByName(reqVo);
            logger.info("***RESP*** 返回respVos is [{}]", respVos);
            DictMulRespVo dictListRespVo = new DictMulRespVo();
            dictListRespVo.setDictMap(respVos);
            return ResponseData.success(dictListRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findDictByName error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件字典异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/getAttrByServerType", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询费用字段", description = "根据服务类型查询所属费用字段", response = FeeAttrRespVo.class)
    public ResponseData<FeeAttrRespVo> getAttrByServerType(@Validated AttrFeeReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  getAttrByServerType request param is [{}]", reqVo.getServiceTypeKey());
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            List<AttrLabelEntity> respVos = attrLabelCache.getAttrListByBelongSysFromCache(reqVo.getServiceTypeKey());
            if (CollectionUtils.isEmpty(respVos)){
                return ResponseData.createErrorCodeResponse(ErrorCode.REQUEST_PARAM_ERROR.getResCode(), ErrorCode.REQUEST_PARAM_ERROR.getResMsg());
            }
            FeeAttrRespVo respVo = new FeeAttrRespVo();
            respVo.setList(respVos.stream().sorted(Comparator.comparing(AttrLabelEntity::getSortKey))
                    .map(ConvertEntityToVo::convert).collect(Collectors.toList()));
            return ResponseData.success(respVo);
        } catch (Exception e) {
            logger.error("***RESP*** getAttrByServerType error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]费用字典异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/queryHideFieldByServerType", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询隐藏字段", description = "根据服务类型查询隐藏字段", response = Map.class)
    public ResponseData<Map<String, List<String>>> queryHideFieldByServerType(@Validated AttrFeeReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  queryHideFieldByServerType request param is [{}]", reqVo.getServiceTypeKey());
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            List<HideFieldConfEntity> respVos = hideFieldConfMapper.queryByServerType(reqVo.getServiceTypeKey());
            logger.info("***RESP*** queryHideFieldByServerType 返回respVos is [{}]", respVos);
            if (CollectionUtils.isEmpty(respVos)){
                return ResponseData.createErrorCodeResponse(ErrorCode.REQUEST_PARAM_ERROR.getResCode(), ErrorCode.REQUEST_PARAM_ERROR.getResMsg());
            }
            Map<String, List<String>> map = respVos.stream().map(h -> {
                HideFieldVo hideFieldVo = new HideFieldVo();
                hideFieldVo.setModuleType(h.getModuleType());
                hideFieldVo.setList(getList(h.getHideField(), SPLIT_COMMA, Function.identity()));
                return hideFieldVo;
            }).collect(Collectors.toMap(HideFieldVo::getModuleType, HideFieldVo::getList, (x1, x2) -> x1));
            return ResponseData.success(map);
        } catch (Exception e) {
            logger.error("***RESP*** queryHideFieldByServerType error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询隐藏字段异常 {}", e);
            return ResponseData.error();
        }
    }

    /**
     * 获取上传图片需要的凭证信息
     *
     * @return
     */
    @RequestMapping(value = "/getUploadAuth", method = RequestMethod.GET)
    @AutoDocMethod(value = "获取上传图片需要的凭证信息", description = "获取上传图片需要的凭证信息")
    public ResponseData<?> getUploadAuth(@Validated UploadReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  getUploadAuth request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        UploadEnum uploadEnum = UploadEnum.getEnumByCode(reqVo.getBusinessKey());
        if (Objects.isNull(uploadEnum)) {
            return ResponseData.createErrorCodeResponse(com.autoyol.commons.web.ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.UPLOAD_TYPE_NOT_EXIST);
        }

        try {
            String dir = "field/console/" + uploadEnum.getPath() + "/" + getDatePath().toString();
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("callToken", callToken);
            reqMap.put("dir", dir);
            reqMap.put("bucket", realBucket);
            String reqContent = JSONObject.toJSONString(reqMap);
            String reqUrl = ossUrl + "/oss/getSign";
            logger.info("getUploadAuth 上传图片前获取凭证信息请求url={},param={}", reqUrl, reqContent);
            String responseContent = HttpUtils.postByJson(reqUrl, reqContent);
            logger.info("getUploadAuth 上传图片前获取凭证信息,返回值={}", responseContent);
            ResponseData responseObj = JSONObject.parseObject(responseContent, ResponseData.class);
            ResponseData retObj = new ResponseData();
            if (responseObj != null) {
                retObj.setResCode(responseObj.getResCode());
                retObj.setResMsg(responseObj.getResMsg());
                JSONObject jsonObj = JSONObject.parseObject(responseContent);
                if (ErrorCode.SUCCESS.getResCode().equals(responseObj.getResCode()) && responseObj.getData() != null && jsonObj != null && jsonObj.getJSONObject("data") != null) {
                    UploadAuthVo uploadAuthVo = JSONObject.toJavaObject(jsonObj.getJSONObject("data"), UploadAuthVo.class);
                    retObj.setData(uploadAuthVo);
                }
            }
            return retObj;
        } catch (Exception e) {
            logger.error("getUploadAuth 上传图片前获取凭证信息错误：e {}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]上传图片前获取凭证信息错误 {}", e);
        }
        return new ResponseData<>(ErrorCode.FAILED.getResCode(), ErrorCode.FAILED.getResMsg());
    }

    @RequestMapping(value = "/queryImportLog", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询导入日志", description = "分页查询导入日志", response = ImportLogRespVo.class)
    public ResponseData<ImportLogRespVo> queryImportLog(@Validated ImportLogReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  queryImportLog request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        try {
            ImportLogRespVo respVos = importService.queryImportLog(reqVo);
            logger.info("***RESP*** 返回respVos is [{}]", respVos);
            return ResponseData.success(respVos);
        } catch (Exception e) {
            logger.error("***RESP*** queryImportLog error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询导入日志异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    public ResponseData set(BasePage basePage) {
        logger.info("***REQ***  set request param is [{}]", basePage);

        try {
            userInfoTmpService.set(basePage);
            return ResponseData.success();
        } catch (Exception e) {
            logger.error("***RESP*** set error:{}, param [{}]", e, basePage);
            Cat.logError("param [" + basePage + "]set异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/syncOrder", method = RequestMethod.GET)
    public ResponseData syncOrder() {
        logger.info("***REQ***  syncOrder request");

        try {
            tackBackInfoService.syncOrder();
            return ResponseData.success();
        } catch (Exception e) {
            logger.error("***RESP*** syncOrder error:{}", e);
            Cat.logError("syncOrder异常 {}", e);
            return ResponseData.error();
        }
    }

    public static StringBuilder getDatePath() {
        String yearMonth = LocalDateUtil.dateToStringFormat(new Date(), DATE_MONTH);
        StringBuilder sb = new StringBuilder(yearMonth);
        sb.insert(2, "/");
        sb.insert(5, "/");
        sb.insert(8, "/");
        sb.insert(11, "/");
        sb.append("/");
        return sb;
    }
}

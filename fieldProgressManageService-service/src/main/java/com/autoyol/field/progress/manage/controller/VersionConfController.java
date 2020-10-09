package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.version.*;
import com.autoyol.field.progress.manage.response.node.RemindVo;
import com.autoyol.field.progress.manage.response.version.VersionMaxRespVo;
import com.autoyol.field.progress.manage.response.version.VersionPageRespVo;
import com.autoyol.field.progress.manage.response.version.VersionVo;
import com.autoyol.field.progress.manage.service.VersionService;
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

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;

@RestController
@RequestMapping("/console/version")
@AutoDocVersion(version = "版本信息维护")
public class VersionConfController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    VersionService versionService;

    @RequestMapping(value = "/findPageByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询版本信息", description = "分页查询版本信息", response = VersionPageRespVo.class)
    public ResponseData<VersionPageRespVo> findPageByCond(VersionPageReqVo reqVo) {
        logger.info("***REQ***  findPageByCond 分页查询版本信息 param {}", reqVo);
        try {
            List<VersionVo> vos = versionService.findPageByCond(reqVo);
            VersionPageRespVo pageRespVo = new VersionPageRespVo();
            setPage(pageRespVo, reqVo, vos);
            pageRespVo.setVersionList(vos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findPageByCond error:{, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]分页查询版本信息 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/findVersionById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单条版本信息", description = "分页查询版本信息", response = RemindVo.class)
    public ResponseData<VersionVo> findVersionById(VersionSelectReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  findVersionById 查询单条版本信息 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            VersionVo version = versionService.findVersionById(reqVo.getId());
            logger.info("***RESP*** 查询单条版本信息 version={}", version);
            return ResponseData.success(version);
        } catch (Exception e) {
            logger.error("***RESP*** findVersionById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单条版本信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @AutoDocMethod(value = "新增版本信息", description = "新增版本信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> add(@Validated @RequestBody VersionAddReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  add 新增版本信息 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = versionService.add(reqVo);
            logger.info("***RESP*** 新增版本信息 count={}", count);
            ResponseData<Integer> x = validCount(count);
            if (x != null) {
                return x;
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** add error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]新增版本信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "修改版本信息", description = "修改版本信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> update(@Validated @RequestBody VersionUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  update 修改版本信息 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = versionService.update(reqVo);
            logger.info("***RESP*** 修改版本信息 count={}", count);
            ResponseData<Integer> x = validCount(count);
            if (x != null) {
                return x;
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** update error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]修改版本信息 异常 [{}]", e);
            return ResponseData.error();
        }
    }

    private ResponseData<Integer> validCount(int count) {
        if (count == NEG_ONE) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.INNER_VERSION_EXIST);
        }
        if (count == NEG_TOW) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OUTER_VERSION_EXIST);
        }
        return null;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @AutoDocMethod(value = "删除版本信息", description = "删除版本信息", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> delete(@Validated @RequestBody VersionDeleteReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  delete 删除版本信息 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }

        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = versionService.delete(reqVo.getId());
            logger.info("***RESP*** 删除版本信息 count={}", count);
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** delete error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]删除版本信息 异常 [{}]", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/getMaxVersion", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询最大版本号", description = "查询最大版本号", response = VersionMaxRespVo.class)
    public ResponseData<VersionMaxRespVo> getMaxVersion() {
        logger.info("***REQ***  getMaxVersion 查询最大版本号");
        try {
            VersionMaxRespVo respVo = versionService.getMaxVersion();
            logger.info("***RESP*** getMaxVersion respVo={}", respVo);
            return ResponseData.success(respVo);
        } catch (Exception e) {
            logger.error("***RESP*** getMaxVersion error:{}", e);
            Cat.logError("查询最大版本号 异常 {}", e);
            return ResponseData.error();
        }
    }
}

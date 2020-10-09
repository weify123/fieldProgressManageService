package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.constraint.FieldErrorCode;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.node.*;
import com.autoyol.field.progress.manage.response.node.RemindPageRespVo;
import com.autoyol.field.progress.manage.response.node.RemindVo;
import com.autoyol.field.progress.manage.service.NodeRemindService;
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
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.*;

@RestController
@RequestMapping("/console/nodeRemind")
@AutoDocVersion(version = "外勤服务节点提醒配置")
public class NodeRemindController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    NodeRemindService nodeRemindService;

    @Value("${node.export.name:外勤服务节点提醒配置}")
    private String excelName;

    @RequestMapping(value = "/findPageByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询节点提醒配置", description = "分页查询节点提醒配置", response = RemindPageRespVo.class)
    public ResponseData<RemindPageRespVo> findPageByCond(RemindPageReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  findPageByCond 节点提醒配置分页查询 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.DATE_FORMAT_ERROR);
        }
        try {
            List<RemindVo> vos = nodeRemindService.findPageByCond(reqVo);
            RemindPageRespVo pageRespVo = new RemindPageRespVo();
            setPage(pageRespVo, reqVo, vos);
            pageRespVo.setRemindList(vos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findPageByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]节点提醒配置分页查询异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/findRemindById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单条节点提醒配置", description = "分页查询节点提醒配置", response = RemindVo.class)
    public ResponseData<RemindVo> findRemindById(RemindSelectReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  findRemindById 查询单条节点提醒配置 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            RemindVo remind = nodeRemindService.findRemindById(reqVo.getId());
            logger.info("***RESP*** 查询单条节点提醒配置 remind={}", remind);
            return ResponseData.success(remind);
        } catch (Exception e) {
            logger.error("***RESP*** findRemindById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单条节点提醒配置异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @AutoDocMethod(value = "新增节点提醒配置", description = "新增节点提醒配置", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> add(@Validated @RequestBody RemindAddReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  add 新增节点提醒配置 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            ResponseData<Integer> x = validDate(reqVo.getNoticeStartTime(), reqVo.getNoticeEndTime());
            if (x != null) {
                return x;
            }
            int count = nodeRemindService.add(reqVo);
            logger.info("***RESP*** 新增节点提醒配置 count={}", count);
            if (count == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.SERVER_NODE_EXIST + "无法新增");
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** add error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]新增节点提醒配置异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @AutoDocMethod(value = "修改节点提醒配置", description = "修改节点提醒配置", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> update(@Validated @RequestBody RemindUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  update 修改节点提醒配置 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            ResponseData<Integer> x = validDate(reqVo.getNoticeStartTime(), reqVo.getNoticeEndTime());
            if (x != null) {
                return x;
            }
            int count = nodeRemindService.update(reqVo);
            logger.info("***RESP*** 修改节点提醒配置 count={}", count);
            if (count == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.SERVER_NODE_EXIST + "无法修改");
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** update error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]修改节点提醒配置异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @AutoDocMethod(value = "删除节点提醒配置", description = "删除节点提醒配置", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> delete(@Validated @RequestBody RemindDeleteReaVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  delete 删除节点提醒配置 param {}", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        try {
            int count = nodeRemindService.deleteById(reqVo);
            logger.info("***RESP*** 删除节点提醒配置 count={}", count);
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** delete error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]删除节点提醒配置异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/exportNode")
    @AutoDocMethod(value = "导出", description = "导出", response = String.class)
    public ResponseData<String> exportNode(RemindPageReqVo reqVo, HttpServletResponse response) {
        logger.info("***REQ***  exportNode request param is [{}]", reqVo);
        try {
            List<RemindVo> vos = nodeRemindService.findPageByCond(reqVo);
            if (CollectionUtils.isEmpty(vos)) {
                return ResponseData.createErrorCodeResponse(FieldErrorCode.EXPORT_DATA_EMPTY.getCode(), FieldErrorCode.EXPORT_DATA_EMPTY.getText());
            }
            ExcelUtil<RemindVo> excelUtil = new ExcelUtil<>(excelName);
            excelUtil.export(vos.stream().peek(vo -> {
                vo.setNoticeStartTimeStr(dateToStringFormat(vo.getNoticeStartTime(), DATE_YYYY_MM_DD_LINE));
                vo.setNoticeEndTimeStr(dateToStringFormat(vo.getNoticeEndTime(), DATE_YYYY_MM_DD_LINE));
            }).collect(Collectors.toList()));
            excelUtil.flushToRequest(response, excelName);
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** exportNode error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]导出节点提醒异常 {}", e);
            return ResponseData.error();
        }
    }

    private ResponseData<Integer> validDate(String startDate, String endDate) throws Exception {
        LocalDate start = convertToLocalDate(convertToDate(startDate));
        LocalDate end = convertToLocalDate(convertToDate(endDate));
        if (start.isAfter(end)) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.START_IS_BEFORE_END);
        }
        if (start.isBefore(LocalDate.now())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.START_IS_BEFORE_NOW);
        }
        return null;
    }
}

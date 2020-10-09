package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.constraint.FieldErrorCode;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.entity.SupplierAccountEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.request.attendance.*;
import com.autoyol.field.progress.manage.response.attendance.*;
import com.autoyol.field.progress.manage.service.AttendanceService;
import com.autoyol.field.progress.manage.service.CommonService;
import com.autoyol.field.progress.manage.util.poi.ExcelMergeUtil;
import com.dianping.cat.Cat;
import com.google.common.collect.Lists;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.*;

@RestController
@RequestMapping("/console/attendance")
@AutoDocVersion(version = "外勤人员考勤管理")
public class AttendanceController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AttendanceService attendanceService;

    @Resource
    CommonService commonService;

    @Value("${attendance.max.day:30}")
    private Long maxDay;

    @Value("${attendance.online.day}")
    private String onlineDay;

    @Value("${attendance.display.day:7}")
    private long displayDay;

    @Value("${user.attendance.export.name:外勤人员考勤管理}")
    private String excelName;

    @Value("${user.attendance.export.template.name:外勤人员考勤管理导入模板}")
    private String excelTemplateName;

    @Resource
    DictCache dictCache;


    @RequestMapping(value = "/getAttendanceConf", method = RequestMethod.GET)
    @AutoDocMethod(value = "获取考勤配置", description = "获取考勤配置", response = OnlineDateReqVo.class)
    public ResponseData<OnlineDateReqVo> getAttendanceConf() {
        logger.info("***REQ***  getAttendanceConf");
        return ResponseData.success(new OnlineDateReqVo(onlineDay));
    }

    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页搜索", description = "分页条件查询外勤人员考勤列表", response = AttendanceQueryPageRespVo.class)
    public ResponseData<AttendanceQueryPageRespVo> queryByPage(AttendanceQueryReqVo reqVo, HttpServletRequest request) {
        String newUserId = request.getHeader(NEW_USER_ID);
        logger.info("***REQ***  queryByPage request param is [{}] newUserId={}", reqVo, newUserId);
        if (StringUtils.isNotEmpty(newUserId)){
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (resetSupplierCond(reqVo, accountEntity)) {
                return ResponseData.success();
            }
        }
        Date date;
        try {
            date = convertToDate(
                    Optional.ofNullable(reqVo.getDateTime()).orElse(dateToStringFormat(new Date(), DATE_YYYY_MM_DD_LINE)));
            LocalDate localDateTime = convertToLocalDate(date);
            LocalDate max = LocalDate.now().plusDays(maxDay - INT_ONE);
            LocalDate min = convertToLocalDate(convertToDate(onlineDay));
            if (localDateTime.isAfter(max) || localDateTime.isBefore(min)) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        "只能查" + onlineDay + "到" + formatter(max, DATE_YYYY_MM_DD_LINE) + "之间的数据");
            }
        } catch (Exception e) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), ErrorCode.PARAM_ERROR.getText());
        }

        try {
            return ResponseData.success(attendanceService.queryByPage(reqVo, date));
        } catch (Exception e) {
            logger.error("***RESP*** queryByPage error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索异常 {}", e);
            return ResponseData.error();
        }

    }

    @RequestMapping(value = "/queryMemoById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询备注", description = "查询单条备注", response = AttendanceMemoRespVo.class)
    public ResponseData<AttendanceMemoRespVo> queryMemoById(@Validated AttendanceReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  queryMemoById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            return ResponseData.success(attendanceService.queryMemoById(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** queryMemoById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]queryMemoById单条查询异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateStatusAndMemoById", method = RequestMethod.POST)
    @AutoDocMethod(value = "更新备注", description = "更新备注", response = Integer.class)
    @UserInfoAutoSet()
    public ResponseData<Integer> updateStatusAndMemoById(@Validated @RequestBody AttendanceUpdateMemoReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  updateMemoById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            return ResponseData.success(attendanceService.updateStatusAndMemoById(reqVo));
        } catch (Exception e) {
            logger.error("***RESP*** updateMemoById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]updateMemoById 单条查询异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/downloadImportTemplate", method = RequestMethod.GET)
    @AutoDocMethod(value = "下载导入考勤模板", description = "下载导入考勤模板", response = String.class)
    public ResponseData<String> downloadImportTemplate(HttpServletResponse response) {
        try {

            List<String> excelHeader = Arrays.asList("人员id", "用户姓名", "服务公司", "城市");

            List<String> excelMerge = Lists.newArrayList();
            IntStream.range(INT_ONE, (int) displayDay + 1).forEach(i -> {
                excelMerge.add(CH_DI + i + DAY);
            });
            List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(ZONE_TYPE);
            List<String> secondTitle = Lists.newArrayList();
            setSecondTitle(excelMerge, dictList, secondTitle);

            List<String> dataList = Lists.newArrayList("123456", "张三", "凹凸", "上海");
            IntStream.range(NEG_ZERO, secondTitle.size()).forEach(x -> {
                dataList.add(CQ);
            });
            ExcelMergeUtil.reportMergeXls(Collections.singletonList(dataList), excelHeader, excelMerge, secondTitle, excelTemplateName, dictList.size(), response);
            logger.info("***RESP***  下载模板成功 ");
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** downloadImportTemplate error:{}", e);
            Cat.logError("downloadImportTemplate 异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/exportAttendance", method = RequestMethod.GET)
    @AutoDocMethod(value = "导出", description = "导出", response = String.class)
    public ResponseData<String> exportAttendance(@Validated AttendanceExportReqVo reqVo, HttpServletRequest request,
                                                 BindingResult bindingResult, HttpServletResponse response) {
        logger.info("***REQ***  exportAttendance request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        String newUserId = request.getHeader(NEW_USER_ID);
        if (StringUtils.isNotEmpty(newUserId)){
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (resetSupplierCond(reqVo, accountEntity)) {
                return ResponseData.success();
            }
        }
        try {
            ResponseData<String> isFalse = validDate(reqVo);
            if (Objects.nonNull(isFalse)) {
                return isFalse;
            }

            Date start = convertToDate(reqVo.getExportStartDate());
            Date end = convertToDate(reqVo.getExportEndDate());
            AttendanceQueryPageRespVo pageRespVo = attendanceService.exportAttendance(reqVo, start, end);
            if (CollectionUtils.isEmpty(pageRespVo.getList())) {
                return ResponseData.createErrorCodeResponse(FieldErrorCode.EXPORT_DATA_EMPTY.getCode(), FieldErrorCode.EXPORT_DATA_EMPTY.getText());
            }

            List<String> excelHeader = Arrays.asList("人员id", "用户姓名", "服务公司", "城市", "状态");

            LocalDate localStartDate = convertToLocalDate(start);
            LocalDate localEndDate = convertToLocalDate(end);
            int day = getInterDay(localEndDate, localStartDate);
            List<String> excelMerge = Lists.newArrayList();
            IntStream.range(NEG_ZERO, day + 1).forEach(i -> {
                excelMerge.add(dateToStringFormat(convertToDate(localStartDate.plusDays(i)), DATE_YYYY_MM_DD_SPOT));
            });

            List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(ZONE_TYPE);
            List<String> secondTitle = Lists.newArrayList();
            setSecondTitle(excelMerge, dictList, secondTitle);

            List<List<String>> dataList = Lists.newArrayList();
            List<AttendanceQueryVo> vos = pageRespVo.getList();
            vos.forEach(vo -> {
                List<String> tmp = Lists.newArrayList();
                tmp.add(vo.getUserId() + STRING_EMPTY);
                tmp.add(vo.getUserName());
                tmp.add(vo.getCompanyName());
                tmp.add(vo.getCityName());
                tmp.add(vo.getStatusVal());
                Optional.ofNullable(vo.getAttendanceList()).ifPresent(lst -> {
                    lst.stream().map(AttendanceVo::getStatusVal).forEach(tmp::add);
                });
                dataList.add(tmp);
            });

            ExcelMergeUtil.reportMergeXls(dataList, excelHeader, excelMerge, secondTitle, excelName, dictList.size(), response);
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** downloadImportTemplate error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]downloadImportTemplate 异常 {}", e);
            return ResponseData.error();
        }
    }

    private void setSecondTitle(List<String> excelMerge, List<SysDictEntity> dictList, List<String> secondTitle) {
        IntStream.range(NEG_ZERO, excelMerge.size()).forEach(i -> {
            dictList.forEach(dict -> {
                secondTitle.add(dict.getLabel());
            });
        });
    }

    private ResponseData<String> validDate(AttendanceExportReqVo reqVo) throws Exception {

        Date startDate = convertToDate(reqVo.getExportStartDate());
        Date endDate = convertToDate(reqVo.getExportEndDate());
        if (startDate.after(endDate)) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    "开始时间大于结束时间");
        }
        LocalDate localStartDate = convertToLocalDate(startDate);
        LocalDate localEndDate = convertToLocalDate(endDate);
        LocalDate max = LocalDate.now().plusDays(maxDay - INT_ONE);
        LocalDate beforeThirty = LocalDate.now().minusDays(maxDay - INT_ONE);
        LocalDate online = convertToLocalDate(convertToDate(onlineDay));
        LocalDate min = online.isBefore(beforeThirty) ? online : beforeThirty;
        if (localStartDate.isAfter(max) || localStartDate.isBefore(min)) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    "开始时间只能" + formatter(min, DATE_YYYY_MM_DD_LINE) + "到" + formatter(max, DATE_YYYY_MM_DD_LINE) + "之间");
        }
        if (localEndDate.isAfter(max) || localEndDate.isBefore(min)) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    "结束时间只能" + formatter(min, DATE_YYYY_MM_DD_LINE) + "到" + formatter(max, DATE_YYYY_MM_DD_LINE) + "之间");
        }
        return null;
    }

    @RequestMapping(value = "/importAttendance", method = RequestMethod.POST)
    @AutoDocMethod(value = "导入", description = "导入", response = String.class)
    @UserInfoAutoSet()
    public ResponseData<String> importAttendance(@Validated AttendanceImportReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  importAttendance request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            Date startDate = convertToDate(reqVo.getImportDate());
            LocalDate limitDate = LocalDate.now().plusDays(displayDay - 1);
            LocalDate inputDate = convertToLocalDate(startDate);
            if (inputDate.isAfter(limitDate)) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        "只能导入" + formatter(limitDate, DATE_YYYY_MM_DD_LINE) + "之前的数据");
            }
            int count = attendanceService.importAttendance(inputDate,
                    getWorkBook(reqVo.getAttachmentFile().getOriginalFilename(), reqVo.getAttachmentFile().getInputStream()), reqVo.getHandleUser() + SPLIT_COMMA + reqVo.getHandleUserNo());
            ResponseData<String> x = validImportNoTow(count);
            if (Objects.nonNull(x)) {
                return x;
            }
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("importAttendance 导入 failed：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]importAttendance 导入 {}", e);
            return ResponseData.error();
        }
    }

}

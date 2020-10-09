package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.utils.StringUtils;
import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.aop.UserInfoAutoSet;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.request.ImportFileReqVo;
import com.autoyol.field.progress.manage.request.store.StoreAddReqVo;
import com.autoyol.field.progress.manage.request.store.StorePageReqVo;
import com.autoyol.field.progress.manage.request.store.StoreSingleReqVo;
import com.autoyol.field.progress.manage.request.store.StoreUpdateReqVo;
import com.autoyol.field.progress.manage.response.store.DownLoadAddRespVo;
import com.autoyol.field.progress.manage.response.store.StoreExportRespVo;
import com.autoyol.field.progress.manage.response.store.StoreInfoPageRespVo;
import com.autoyol.field.progress.manage.response.store.StoreInfoPageVo;
import com.autoyol.field.progress.manage.service.StoreService;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;
import com.autoyol.field.progress.manage.util.LocalDateUtil;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;

@RestController
@RequestMapping("/console/store")
@AutoDocVersion(version = "门店信息管理")
public class StoreInfoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    StoreService storeService;

    @Value("${store.export.name:门店信息}")
    private String excelName;

    @Value("${store.download.add.name:批量新增模板}")
    private String downLoadAddExcelName;

    @Value("${store.download.update.name:批量修改模板}")
    private String downLoadUpdateExcelName;

    @Value("${store.download.opr.name:批量操作模板}")
    private String downLoadZipName;

    @RequestMapping(value = "/findStoreByCond", method = RequestMethod.GET)
    @AutoDocMethod(value = "分页查询门店信息列表", description = "根据条件查询门店列表信息", response = StoreInfoPageRespVo.class)
    public ResponseData<StoreInfoPageRespVo> findStoreByCond(StorePageReqVo reqVo) {
        logger.info("***REQ***  findStoreByCond request param is [{}]", reqVo);
        try {
            List<StoreInfoPageVo> respVos = storeService.findStoreByCond(reqVo);
            StoreInfoPageRespVo pageRespVo = new StoreInfoPageRespVo();
            setPage(pageRespVo, reqVo, respVos);
            pageRespVo.setPageList(respVos.stream()
                    .skip((pageRespVo.getPageNo() - INT_ONE) * reqVo.getPageSize())
                    .limit(reqVo.getPageSize())
                    .collect(Collectors.toList()));
            return ResponseData.success(pageRespVo);
        } catch (Exception e) {
            logger.error("***RESP*** findStoreByCond error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]条件搜索门店信息列表异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/selectById", method = RequestMethod.GET)
    @AutoDocMethod(value = "查询单个门店信息", description = "查询单个门店信息", response = StoreInfoPageVo.class)
    public ResponseData<StoreInfoPageVo> selectById(@Validated StoreSingleReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  selectById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        try {
            StorePageReqVo pageReqVo = new StorePageReqVo();
            pageReqVo.setStoreId(reqVo.getStoreId());
            List<StoreInfoPageVo> respVos = storeService.findStoreByCond(pageReqVo);
            logger.info("selectById 返回 {}", respVos);
            return ResponseData.success(respVos.stream().findFirst().orElse(null));
        } catch (Exception e) {
            logger.error("***RESP*** selectById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]查询单个门店信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    @AutoDocMethod(value = "修改单个门店信息", description = "修改单个门店信息", response = Integer.class)
    @UserInfoAutoSet
    public ResponseData<Integer> updateById(@Validated @RequestBody StoreUpdateReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  updateById request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (validTime(reqVo.getStartTime(), reqVo.getEndTime())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.START_OR_END_TIME_FORMAT_ERROR);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = storeService.updateById(reqVo);
            logger.info("updateById 返回 {}", count);
            if (count == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.STORE_NAME_EXIST);
            }
            if (count == NEG_TOW) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.STORE_FULL_NAME_EXIST);
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** updateById error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]修改单个门店信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @AutoDocMethod(value = "新增单个门店", description = "新增单个门店", response = Integer.class)
    @UserInfoAutoSet
    public ResponseData<Integer> add(@Validated @RequestBody StoreAddReqVo reqVo, BindingResult bindingResult) {
        logger.info("***REQ***  新增单个门店 request param is [{}]", reqVo);
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (validTime(reqVo.getStartTime(), reqVo.getEndTime())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.START_OR_END_TIME_FORMAT_ERROR);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = storeService.add(reqVo);
            logger.info("新增单个门店 返回 {}", count);
            if (count == NEG_ONE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.STORE_NAME_EXIST);
            }
            if (count == NEG_TOW) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.STORE_FULL_NAME_EXIST);
            }
            return ResponseData.success(count);
        } catch (Exception e) {
            logger.error("***RESP*** 新增单个门店 error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]新增单个门店信息异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/downLoadTemplate", method = RequestMethod.GET)
    @AutoDocMethod(value = "下载模板", description = "下载模板", response = String.class)
    public ResponseData<String> downLoadTemplate(HttpServletResponse response) {
        DownLoadAddRespVo addVo = buildDownLoadAddRespVo();
        StoreExportRespVo updateVo = new StoreExportRespVo();
        updateVo.setId(12345);
        try {
            ConvertBeanUtil.copyProperties(updateVo, addVo);

            ServletOutputStream sos = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(sos);
            response.reset();
            response.setContentType("application/octet-stream ");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((downLoadZipName + ".zip").getBytes(), CacheConstraint.ISO_8859));

            ExcelUtil<DownLoadAddRespVo> addUtil = new ExcelUtil<>(downLoadAddExcelName);
            addUtil.export(Collections.singletonList(addVo));
            ZipEntry entry = new ZipEntry(downLoadAddExcelName + ".xls");
            zipOutputStream.putNextEntry(entry);
            addUtil.wb.write(zipOutputStream);

            ExcelUtil<StoreExportRespVo> excelUtil = new ExcelUtil<>(downLoadUpdateExcelName);
            excelUtil.export(Collections.singletonList(updateVo));

            ZipEntry entry1 = new ZipEntry(downLoadUpdateExcelName + ".xls");
            zipOutputStream.putNextEntry(entry1);
            excelUtil.wb.write(zipOutputStream);

            zipOutputStream.flush();
            zipOutputStream.close();
            sos.close();

            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("***RESP*** 导出门店 error:{}", e);
            Cat.logError("导出门店异常 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/importBatchAdd", method = RequestMethod.POST)
    @AutoDocMethod(value = "批量新增", description = "导入城市", response = String.class)
    @UserInfoAutoSet()
    public ResponseData<String> importBatchAdd(@Validated ImportFileReqVo reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = storeService.importBatchAdd(
                    getWorkBook(reqVo.getAttachmentFile().getOriginalFilename(), reqVo.getAttachmentFile().getInputStream()), reqVo.getHandleUser());
            ResponseData<String> x = validImportNoTow(count);
            if (Objects.nonNull(x)) {
                return x;
            }
            if (count == NEG_FIVE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.EXCEL_CONTENT_STORE_NAME_MUL);
            }
            if (count == NEG_SIX) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.EXCEL_CONTENT_STORE_FULL_NAME_MUL);
            }

            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("importBatchAdd 批量新增 failed：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]importBatchAdd 导入 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/importBatchUpdate", method = RequestMethod.POST)
    @AutoDocMethod(value = "批量修改", description = "批量修改", response = String.class)
    @UserInfoAutoSet()
    public ResponseData<String> importBatchUpdate(@Validated ImportFileReqVo reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }

        try {
            int count = storeService.importBatchUpdate(
                    getWorkBook(reqVo.getAttachmentFile().getOriginalFilename(), reqVo.getAttachmentFile().getInputStream()), reqVo.getHandleUser());
            ResponseData<String> x = validImportNoTow(count);
            if (Objects.nonNull(x)) {
                return x;
            }
            if (count == NEG_FIVE) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.EXCEL_CONTENT_STORE_NAME_MUL);
            }
            if (count == NEG_SIX) {
                return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                        MsgConstraint.EXCEL_CONTENT_STORE_FULL_NAME_MUL);
            }
            return ResponseData.success(ErrorCode.SUCCESS.getText());
        } catch (Exception e) {
            logger.error("importBatchUpdate 导入 failed：{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]importBatchUpdate 导入 {}", e);
            return ResponseData.error();
        }
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @AutoDocMethod(value = "导出", description = "导出", response = String.class)
    public ResponseData<String> export(StorePageReqVo reqVo, HttpServletResponse response) {
        logger.info("***REQ***  导出 request param is [{}]", reqVo);
        try {
            List<StoreInfoPageVo> respVos = storeService.findStoreByCond(reqVo);
            List<StoreExportRespVo> list = respVos.stream().map(store -> {
                StoreExportRespVo exportVo = new StoreExportRespVo();
                try {
                    ConvertBeanUtil.copyProperties(exportVo, store);
                    exportVo.setServeStatus(store.getServeStatusVal());
                } catch (Exception e) {
                    logger.error("***PRO*** 转换失败 {}", e);
                }
                return exportVo;
            }).collect(Collectors.toList());

            return exportFlush(list, excelName, response);
        } catch (Exception e) {
            logger.error("***RESP*** 导出门店 error:{}, param [{}]", e, reqVo);
            Cat.logError("param [" + reqVo + "]导出门店异常 {}", e);
            return ResponseData.error();
        }
    }

    private boolean validTime(String startTime, String endTime) {
        try {
            LocalDateUtil.parseLocalTime(startTime, DATE_TIME_NO_SECOND);
            LocalDateUtil.parseLocalTime(endTime, DATE_TIME_NO_SECOND);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    private DownLoadAddRespVo buildDownLoadAddRespVo() {
        DownLoadAddRespVo addVo = new DownLoadAddRespVo();
        addVo.setStoreName(STRING_EMPTY);
        addVo.setStoreFullName("遵义乾坤荣馨汽车有限公司");
        addVo.setBranchCompany("贵州分公司");
        addVo.setCity("遵义");
        addVo.setCityLevel(STRING_EMPTY);
        addVo.setDistrictCounty(STRING_EMPTY);
        addVo.setStoreAddress("遵义市遵南大道黔北国际汽车博览中心");
        addVo.setLongitude(new BigDecimal("106.8827"));
        addVo.setLatitude(new BigDecimal("27.6138"));
        addVo.setStoreContactName("张三");
        addVo.setStoreContactMobile("12345678900");
        addVo.setStartTime("09:00");
        addVo.setEndTime("17:30");
        addVo.setServeStatusVal("是");
        return addVo;
    }
}

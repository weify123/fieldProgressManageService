package com.autoyol.field.progress.manage.controller;

import com.autoyol.commons.web.ErrorCode;
import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.constraint.FieldErrorCode;
import com.autoyol.field.progress.manage.constraint.MsgConstraint;
import com.autoyol.field.progress.manage.entity.SupplierAccountEntity;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.page.BasePage;
import com.autoyol.field.progress.manage.page.PageResult;
import com.autoyol.field.progress.manage.request.BaseRequest;
import com.autoyol.field.progress.manage.request.ImportFileReqVo;
import com.autoyol.field.progress.manage.request.tackback.BaseTackReqVo;
import com.autoyol.field.progress.manage.request.user.BaseUserQueryVo;
import com.autoyol.field.progress.manage.service.AbstractService;
import com.autoyol.field.progress.manage.util.poi.ExcelUtil;
import com.autoyol.field.progress.manage.util.poi.PoiUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.parse;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

public class BaseController {

    protected static <T> ResponseData<T> validate(BindingResult bindingResult) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.toList())) {
            stringBuilder.append(s);
            stringBuilder.append(CacheConstraint.SPLIT_COMMA);
        }
        return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), stringBuilder.substring(0, stringBuilder.length() - 1));
    }

    protected static <T, P> ResponseData<T> validate(Validator validator, P p) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<ConstraintViolation<P>> set = validator.validate(p);
        if (set.isEmpty()) {
            return null;
        }
        set.stream().map(ConstraintViolation::getMessage).forEach(s -> {
            stringBuilder.append(s);
            stringBuilder.append(CacheConstraint.SPLIT_COMMA);
        });
        return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(), stringBuilder.substring(0, stringBuilder.length() - 1));
    }

    protected static Workbook getWorkBook(String fileName, InputStream inputStream) throws IOException {
        return PoiUtil.getWorkBook(fileName, inputStream);
    }

    protected static ResponseData<String> validImport(int count) {
        if (count == NEG_ONE) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.EXCEL_IS_EMPTY);
        }
        if (count == NEG_TOW) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.IMPORT_FAIL);
        }
        if (count == NEG_THREE) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.EXIST_PART_FAIL);
        }
        if (count == NEG_FOUR) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.IMPORT_FAIL);
        }
        return null;
    }

    protected static ResponseData<String> validImportNoTow(int count) {
        if (count == NEG_ONE) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.EXCEL_IS_EMPTY);
        }
        if (count == NEG_THREE) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.EXIST_PART_FAIL);
        }
        if (count == NEG_FOUR) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.IMPORT_FAIL);
        }
        return null;
    }

    protected static <T extends BaseRequest> ResponseData<Integer> validFieldAndUser(@Validated T reqVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validate(bindingResult);
        }
        if (StringUtils.isEmpty(reqVo.getHandleUser())) {
            return ResponseData.createErrorCodeResponse(ErrorCode.PARAM_ERROR.getCode(),
                    MsgConstraint.OPERATOR_IS_EMPTY);
        }
        return null;
    }

    protected static void setPage(PageResult page, BasePage basePage, List<?> list) {
        page.setPageSize(basePage.getPageSize());
        page.setTotal(list.size());
        page.setPageNo(getPageNo(basePage.getPageNo(), page.getPages()));
    }

    protected static boolean resetSupplierCond(BaseUserQueryVo reqVo, SupplierAccountEntity accountEntity) {
        if (Objects.isNull(accountEntity)) {
            return false;
        }
        reqVo.setCompanyId(accountEntity.getCompanyId());
        List<Integer> manageCity = getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt);
        List<Integer> reqCityList = getObjOrDefault(reqVo.getCityIdStr(), reqCity -> getList(reqCity, SPLIT_COMMA, Integer::parseInt), null);
        if (CollectionUtils.isEmpty(manageCity)) {
            return true;
        }
        reqVo.setCityIdStr(Optional.ofNullable(reqCityList)
                .flatMap(reqList -> reqList.stream().filter(city -> hitListSearch(manageCity, city, List::contains))
                        .map(String::valueOf).reduce((x, y) -> x + SPLIT_COMMA + y))
                .orElse(manageCity.stream().map(String::valueOf).reduce((x, y) -> x + SPLIT_COMMA + y).orElse(null)));
        return com.autoyol.commons.utils.StringUtils.isEmpty(reqVo.getCityIdStr());
    }

    protected static <T> ResponseData<String> exportFlush(List<T> list, String excelName, HttpServletResponse resp) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            return ResponseData.createErrorCodeResponse(FieldErrorCode.EXPORT_DATA_EMPTY.getCode(), FieldErrorCode.EXPORT_DATA_EMPTY.getText());
        }
        ExcelUtil<T> excelUtil = new ExcelUtil<>(excelName);
        excelUtil.export(list);
        excelUtil.flushToRequest(resp, excelName);
        return ResponseData.success(ErrorCode.SUCCESS.getText());
    }


    protected static ResponseData<String> importObj(AbstractService abstractService, ImportFileReqVo reqVo) throws Exception {
        int count = abstractService.importObj(
                getWorkBook(reqVo.getAttachmentFile().getOriginalFilename(), reqVo.getAttachmentFile().getInputStream()), reqVo.getHandleUser());
        ResponseData<String> x = validImport(count);
        if (Objects.nonNull(x)) {
            return x;
        }
        return ResponseData.success(ErrorCode.SUCCESS.getText());
    }

    protected static Date checkAndGetDate(BaseTackReqVo reqVo, BindingResult bindingResult) {
        ResponseData<Integer> x = validFieldAndUser(reqVo, bindingResult);
        if (Objects.nonNull(x)) {
            throw new TackBackException(x.getResCode(), x.getResMsg());
        }

        try {
            return parse(reqVo.getPickTime(), DATE_YYYY_MM_DD_CONTACT);
        } catch (Exception e) {
            throw new TackBackException(ErrorCode.PARAM_ERROR.getCode(), MsgConstraint.DATE_FORMAT_ERROR);
        }
    }
}

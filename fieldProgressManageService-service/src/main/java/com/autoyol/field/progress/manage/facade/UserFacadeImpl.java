package com.autoyol.field.progress.manage.facade;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.doc.annotation.AutoDocMethod;
import com.autoyol.doc.annotation.AutoDocVersion;
import com.autoyol.field.progress.manage.controller.BaseController;
import com.autoyol.field.progress.manage.request.login.UserLoginReqVo;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import com.autoyol.field.progress.manage.service.AppUserService;
import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;

import static com.autoyol.field.progress.manage.constraint.FieldErrorCode.USER_NOT_EXIST;
import static com.autoyol.field.progress.manage.constraint.FieldErrorCode.USER_PW_ERROR;

@RestController
@AutoDocVersion(version = "app用户操作")
public class UserFacadeImpl extends BaseController implements AppUserFacade {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AppUserService appUserService;

    @Resource
    Validator validator;

    @AutoDocMethod(value = "用户登录", description = "用户登录", response = AppUserVo.class)
    @Override
    @RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
    public ResponseData<AppUserVo> login(@RequestBody UserLoginReqVo reqVo) {
        logger.info("***REQ***  login request param is [{}]", reqVo);
        ResponseData<AppUserVo> data = validate(validator, reqVo);
        if (Objects.nonNull(data)) {
            return data;
        }
        try {
            List<AppUserVo> userVos = appUserService.userLogin(reqVo);
            if (Objects.isNull(userVos)) {
                return ResponseData.createErrorCodeResponse(USER_NOT_EXIST.getCode(), USER_NOT_EXIST.getText());
            }
            if (userVos.size() == 0) {
                return ResponseData.createErrorCodeResponse(USER_PW_ERROR.getCode(), USER_PW_ERROR.getText());
            }
            return ResponseData.success(userVos.get(0));
        } catch (Exception e) {
            logger.error("***RESP*** login error: {}", e);
            Cat.logError("用户登录异常 {}", e);
            return ResponseData.error();
        }

    }
}

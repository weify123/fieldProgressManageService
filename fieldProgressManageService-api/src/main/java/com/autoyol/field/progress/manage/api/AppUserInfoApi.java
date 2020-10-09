package com.autoyol.field.progress.manage.api;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.fallback.AppUserInfoApiFailBack;
import com.autoyol.field.progress.manage.request.user.BaseUserInfoReqVo;
import com.autoyol.field.progress.manage.request.user.UserInfoSearchReqVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoPageRespVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "fieldProgressManageService-service", fallback = AppUserInfoApiFailBack.class)
public interface AppUserInfoApi {

    @RequestMapping(value = "/console/appUserInfo/findUserInfoByPage", method = RequestMethod.GET)
    ResponseData<AppUserInfoPageRespVo> findUserInfoByPage(@RequestBody UserInfoSearchReqVo userReqVO);

    @RequestMapping(value = "/findUserInfoById", method = RequestMethod.GET)
    ResponseData<AppUserInfoVo> findUserInfoById(@RequestBody BaseUserInfoReqVo reqVo);
}

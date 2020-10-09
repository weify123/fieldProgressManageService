package com.autoyol.field.progress.manage.api;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.fallback.AppUserApiFailBack;
import com.autoyol.field.progress.manage.request.user.*;
import com.autoyol.field.progress.manage.response.user.AppUserPageRespVo;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "fieldProgressManageService-service", fallback = AppUserApiFailBack.class)
public interface AppUserApi {

    @RequestMapping(value = "/console/appUser/findUserByPage", method = RequestMethod.GET)
    ResponseData<AppUserPageRespVo> findUserByPage(@RequestBody AppUserQueryReqVo appUserRequest);

    @RequestMapping(value = "/console/appUser/findUserById", method = RequestMethod.GET)
    ResponseData<AppUserVo> findUserById(@RequestBody BaseAppUserReqVo reqVo);

    @RequestMapping(value = "/console/appUser/updateByUserId", method = RequestMethod.POST)
    ResponseData<Integer> updateByUserId(@RequestBody AppUserUpdateReqVo updateReqVo);

    @RequestMapping(value = "/console/appUser/addAppUser", method = RequestMethod.POST)
    ResponseData<Integer> addAppUser(@RequestBody AppUserAddReqVo userAddReqVo);

    @RequestMapping(value = "/console/appUser/setUserPassWord", method = RequestMethod.POST)
    ResponseData<Integer> setUserPassWord(@RequestBody AppUserSetPassWordReqVo passWordReqVo);

    @RequestMapping(value = "/console/appUser/exportAppUser", method = RequestMethod.GET)
    ResponseData<String> exportAppUser(@RequestBody AppUserQueryReqVo appUserRequest);
}

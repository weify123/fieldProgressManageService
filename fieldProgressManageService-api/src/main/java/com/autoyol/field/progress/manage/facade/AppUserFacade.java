package com.autoyol.field.progress.manage.facade;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.request.login.UserLoginReqVo;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "fieldProgressManageService")
public interface AppUserFacade {

    @RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
    ResponseData<AppUserVo> login(UserLoginReqVo reqVo);
}

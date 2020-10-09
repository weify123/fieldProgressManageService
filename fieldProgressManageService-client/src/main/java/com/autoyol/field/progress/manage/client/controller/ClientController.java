package com.autoyol.field.progress.manage.client.controller;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.api.AppUserApi;
import com.autoyol.field.progress.manage.api.HelloFeignService;
import com.autoyol.field.progress.manage.request.user.AppUserQueryReqVo;
import com.autoyol.field.progress.manage.response.user.AppUserPageRespVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实例程序生成的客户端调用
 **/
@RestController
public class ClientController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HelloFeignService feignService;

    @Autowired
    private AppUserApi appUserApi;

    @GetMapping("/client")
    public ResponseData<String> getName() {
        logger.info("feignService is [{}]", feignService);
        return feignService.hello("world");
    }

    @GetMapping("/find")
    public ResponseData<AppUserPageRespVo> findUserByPage() {
        logger.info("feignService is [{}]", appUserApi);
        AppUserQueryReqVo appUserReqVO = new AppUserQueryReqVo();
        return appUserApi.findUserByPage(appUserReqVO);
    }
}

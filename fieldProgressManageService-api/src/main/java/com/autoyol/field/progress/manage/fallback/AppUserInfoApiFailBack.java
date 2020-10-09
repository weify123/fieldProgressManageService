package com.autoyol.field.progress.manage.fallback;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.api.AppUserInfoApi;
import com.autoyol.field.progress.manage.request.user.BaseUserInfoReqVo;
import com.autoyol.field.progress.manage.request.user.UserInfoSearchReqVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoPageRespVo;
import com.autoyol.field.progress.manage.response.userinfo.AppUserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppUserInfoApiFailBack implements AppUserInfoApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseData<AppUserInfoPageRespVo> findUserInfoByPage(UserInfoSearchReqVo reqVO) {
        logger.info("call fieldProgressManageService.findUserInfoByPage fallback param :reqVO:[{}]", reqVO);
        return ResponseData.error();
    }

    @Override
    public ResponseData<AppUserInfoVo> findUserInfoById(BaseUserInfoReqVo reqVo) {
        logger.info("call fieldProgressManageService.findUserInfoById fallback param :reqVO:[{}]", reqVo);
        return ResponseData.error();
    }
}

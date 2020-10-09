package com.autoyol.field.progress.manage.fallback;

import com.autoyol.commons.web.ResponseData;
import com.autoyol.field.progress.manage.api.AppUserApi;
import com.autoyol.field.progress.manage.request.user.*;
import com.autoyol.field.progress.manage.response.user.AppUserPageRespVo;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppUserApiFailBack implements AppUserApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ResponseData<AppUserPageRespVo> findUserByPage(AppUserQueryReqVo reqVO) {
        logger.info("call fieldProgressManageService.findUserByPage fallback param :reqVO:[{}]", reqVO);
        return ResponseData.error();
    }

    @Override
    public ResponseData<AppUserVo> findUserById(BaseAppUserReqVo reqVo) {
        logger.info("call fieldProgressManageService.findUserById fallback param :userId:[{}]", reqVo.getUserId());
        return ResponseData.error();
    }

    @Override
    public ResponseData<Integer> updateByUserId(AppUserUpdateReqVo updateReqVo) {
        logger.info("call fieldProgressManageService.updateByUserId fallback param :reqVO:[{}]", updateReqVo);
        return ResponseData.error();
    }

    @Override
    public ResponseData<Integer> addAppUser(AppUserAddReqVo userAddReqVo) {
        logger.info("call fieldProgressManageService.userAddReqVo fallback param :reqVO:[{}]", userAddReqVo);
        return ResponseData.error();
    }

    @Override
    public ResponseData<Integer> setUserPassWord(AppUserSetPassWordReqVo passWordReqVo) {
        logger.info("call fieldProgressManageService.setUserPassWord fallback param :reqVO:[{}]", passWordReqVo);
        return ResponseData.error();
    }

    @Override
    public ResponseData<String> exportAppUser(AppUserQueryReqVo appUserRequest) {
        logger.info("call fieldProgressManageService.exportAppUser fallback param :reqVO:[{}]", appUserRequest);
        return ResponseData.error();
    }
}

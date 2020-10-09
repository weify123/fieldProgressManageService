package com.autoyol.field.progress.manage.convert;


import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.entity.AppUserEntity;
import com.autoyol.field.progress.manage.entity.AppUserInfoEntity;
import com.autoyol.field.progress.manage.request.attendance.AttendanceExportReqVo;
import com.autoyol.field.progress.manage.request.attendance.AttendanceQueryReqVo;
import com.autoyol.field.progress.manage.request.user.*;
import com.autoyol.field.progress.manage.security.EncryptUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.function.Function;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.NEG_ONE;
import static com.autoyol.field.progress.manage.cache.CacheConstraint.SPLIT_COMMA;
import static com.autoyol.field.progress.manage.util.OprUtil.getList;
import static com.autoyol.field.progress.manage.util.OprUtil.getObjOrDefault;

public class ConvertAppUser {

    public static AppUserInfoEntity convertUserInfoToUser(UserInfoSearchReqVo reqVo) {
        AppUserInfoEntity entity = new AppUserInfoEntity();
        entity.setStart(reqVo.getStart());
        entity.setPageSize(reqVo.getPageSize());
        entity.setUserId(getObjOrDefault(reqVo.getUserId(), Integer::parseInt, null));
        entity.setUserName(reqVo.getUserName());
        entity.setCityIdList(getList(reqVo.getCityIdStr(), CacheConstraint.SPLIT_COMMA, Integer::parseInt));
        entity.setCompanyId(reqVo.getCompanyId());
        entity.setStatusKey(reqVo.getStatusKey());
        entity.setUserMobile(reqVo.getUserMobile());

        entity.setContactName(reqVo.getContactName());
        entity.setContactMobile(reqVo.getContactMobile());
        entity.setIdNo(reqVo.getIdNo());
        entity.setEmployment(getObjOrDefault(reqVo.getEmployment(), Integer::parseInt, null));
        entity.setIdCardUploaded(reqVo.getIdCardUploaded());
        entity.setDriverUploaded(reqVo.getDriverUploaded());
        entity.setAvatarUploaded(reqVo.getAvatarUploaded());
        entity.setDistributable(reqVo.getDistributable());
        return entity;
    }

    public static AppUserQueryReqVo buildUser(AttendanceQueryReqVo reqVo) {
        AppUserQueryReqVo user = new AppUserQueryReqVo();
        user.setUserId(reqVo.getUserId());
        user.setUserName(reqVo.getUserName());
        user.setCompanyId(reqVo.getCompanyId());
        user.setStatusKey(getObjOrDefault(reqVo.getStatusKey(), Function.identity(), NEG_ONE));
        user.setCityIdList(getList(reqVo.getCityIdStr(), SPLIT_COMMA, Integer::parseInt));
        return user;
    }

    public static AppUserEntity buildUser(AttendanceExportReqVo reqVo) {
        AppUserEntity user = new AppUserEntity();
        user.setId(reqVo.getUserId());
        user.setUserName(reqVo.getUserName());
        user.setEmploymentCompanyId(reqVo.getCompanyId());
        user.setCurStatus(getObjOrDefault(reqVo.getStatusKey(), String::valueOf, String.valueOf(NEG_ONE)));
        user.setCityIdList(getList(reqVo.getCityIdStr(), SPLIT_COMMA, Integer::parseInt));
        return user;
    }

    public static AppUserEntity setPassWordConvert(AppUserSetPassWordReqVo passWordReqVo) throws Exception {
        AppUserEntity userEntity = new AppUserEntity();
        userEntity.setId(passWordReqVo.getUserId());
        userEntity.setPassWord(EncryptUtil.encode(DigestUtils.sha1Hex(passWordReqVo.getPassWord())));
        userEntity.setUpdateOp(passWordReqVo.getHandleUser());
        return userEntity;
    }

    public static AppUserEntity addConvert(AppUserAddReqVo userAddReqVo) throws Exception {
        AppUserEntity userEntity = new AppUserEntity();
        userEntity.setUserName(userAddReqVo.getUserName());
        userEntity.setEmploymentCompanyId(userAddReqVo.getCompanyId());
        userEntity.setCityId(userAddReqVo.getCityId());
        userEntity.setMobile(userAddReqVo.getMobile());
        String pass = userAddReqVo.getMobile().substring(userAddReqVo.getMobile().length() - 6);
        userEntity.setPassWord(EncryptUtil.encode(DigestUtils.sha1Hex(pass)));
        userEntity.setCurStatus(String.valueOf(userAddReqVo.getStatusKey()));
        userEntity.setCreateOp(userAddReqVo.getHandleUser());
        userEntity.setCreateTime(new Date());
        return userEntity;
    }

    public static AppUserEntity updateConvert(AppUserUpdateReqVo updateReqVo) {
        AppUserEntity userEntity = new AppUserEntity();
        userEntity.setId(updateReqVo.getUserId());
        userEntity.setUserName(updateReqVo.getUserName());
        userEntity.setEmploymentCompanyId(updateReqVo.getCompanyId());
        userEntity.setCityId(updateReqVo.getCityId());
        userEntity.setMobile(updateReqVo.getMobile());
        userEntity.setCurStatus(String.valueOf(updateReqVo.getStatusKey()));
        userEntity.setUpdateOp(updateReqVo.getHandleUser());
        userEntity.setUpdateTime(new Date());
        return userEntity;
    }
}

package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.CacheConstraint;
import com.autoyol.field.progress.manage.cache.CityCache;
import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.*;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.exception.TackBackException;
import com.autoyol.field.progress.manage.mapper.AppUserInfoMapper;
import com.autoyol.field.progress.manage.mapper.AppUserMapper;
import com.autoyol.field.progress.manage.mapper.EmployCompanyMapper;
import com.autoyol.field.progress.manage.request.login.UserLoginReqVo;
import com.autoyol.field.progress.manage.request.user.AppUserAddReqVo;
import com.autoyol.field.progress.manage.request.user.AppUserQueryReqVo;
import com.autoyol.field.progress.manage.request.user.AppUserSetPassWordReqVo;
import com.autoyol.field.progress.manage.request.user.AppUserUpdateReqVo;
import com.autoyol.field.progress.manage.response.user.AppUserPageRespVo;
import com.autoyol.field.progress.manage.response.user.AppUserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.constraint.FieldErrorCode.*;
import static com.autoyol.field.progress.manage.convert.ConvertAppUser.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class AppUserService extends AbstractService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AppUserMapper appUserMapper;

    @Resource
    AppUserInfoMapper appUserInfoMapper;

    @Resource
    EmployCompanyMapper employCompanyMapper;

    @Resource
    CityCache cityCache;

    @Resource
    DictCache dictCache;

    @Resource
    CommonService commonService;

    @Resource
    AttendanceService attendanceService;

    @Resource
    MsgService msgService;

    @Value("${db.renyun.switch}")
    private String dbRenYunSwitch;

    public List<AppUserVo> userLogin(UserLoginReqVo reqVo) throws Exception {
        AppUserEntity queryReqVo = new AppUserEntity();
        queryReqVo.setMobile(reqVo.getMobile());
        List<AppUserEntity> entities = appUserMapper.queryAppUserByCond(queryReqVo);
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(USER_STATUS);

        if (!CollectionUtils.isEmpty(entities)) {
            List<AppUserEntity> vos = entities.stream()
                    .filter(appUser -> hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), appUser.getIsDelete(), Integer::equals))
                    .filter(user -> Integer.parseInt(user.getCurStatus()) < INT_TOW)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(vos)) {
                return null;
            }
            return vos.stream()
                    .filter(user -> StringUtils.equalsIgnoreCase(user.getPassWord(), reqVo.getPassWd()))
                    .map(user -> buildUser(dictList, user))
                    .collect(Collectors.toList());
        }
        return null;

    }

    public AppUserPageRespVo findUserByPage(AppUserQueryReqVo reqVo) throws Exception {
        AppUserPageRespVo respVo = new AppUserPageRespVo();
        respVo.setPageNo(reqVo.getPageNo());
        respVo.setPageSize(reqVo.getPageSize());
        reqVo.setCityIdList(getList(reqVo.getCityIdStr(), CacheConstraint.SPLIT_COMMA, Integer::parseInt));
        int count = appUserMapper.countAppUserPageByCond(reqVo);
        if (count <= NEG_ZERO) return respVo;
        respVo.setTotal(count);
        List<AppUserEntity> entities = appUserMapper.queryAppUserPageByCond(reqVo);
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(USER_STATUS);
        if (CollectionUtils.isEmpty(entities)) {
            return respVo;
        }
        respVo.setUserList(entities.stream()
                .map(u -> buildUser(dictList, u))
                .collect(Collectors.toList()));
        return respVo;

    }

    private AppUserVo buildUser(List<SysDictEntity> dictList, AppUserEntity u) {
        AppUserVo respVo = new AppUserVo();
        BeanUtils.copyProperties(u, respVo);
        respVo.setUserId(u.getId());
        respVo.setStatusVal(Optional.ofNullable(u.getCurStatus())
                .map(status -> getLabelByCode(dictList, status))
                .orElse(STRING_EMPTY));
        respVo.setStatusKey(Integer.parseInt(u.getCurStatus()));
        respVo.setCityId(u.getCityId());
        respVo.setCompanyId(u.getEmploymentCompanyId());
        respVo.setCityName(commonService.getCityName(u.getCityId()));
        EmployCompanyEntity companyEntity = commonService.getCompany(u.getEmploymentCompanyId());
        respVo.setCompanyNameFirst(getObjOrDefault(companyEntity, EmployCompanyEntity::getFirstCategory, null));
        respVo.setCompanyName(getObjOrDefault(companyEntity, EmployCompanyEntity::getSecondCategory, null));
        return respVo;
    }

    public AppUserVo findUserById(Integer userId, String newUserId) throws Exception {
        if (Objects.isNull(userId)) {
            return null;
        }
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(USER_STATUS);
        AppUserEntity userEntity = appUserMapper.selectByPrimaryKey(userId);
        if (Objects.nonNull(userEntity) && !StringUtils.isEmpty(newUserId)) {
            SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(newUserId);
            if (Objects.nonNull(accountEntity)) {
                boolean is = objEquals(accountEntity.getCompanyId(), userEntity.getEmploymentCompanyId(), Integer::equals) &&
                        objEquals(getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt), userEntity.getCityId(), List::contains);
                if (!is) {
                    userEntity = null;
                }
            }

        }

        return getObjOrDefault(userEntity, u -> buildUser(dictList, u), null);

    }

    @Transactional
    public void updateByUserId(AppUserUpdateReqVo reqVo) throws Exception {
        AppUserEntity userEntity = updateConvert(reqVo);
        checkSupplier(userEntity, commonService, reqVo.getHandleUserNo());

        AppUserEntity entity = appUserMapper.selectByPrimaryKey(reqVo.getUserId());
        if (Objects.isNull(entity)) {
            throw new TackBackException(OPERATE_NO_PERMIT);
        }
        if (objEquals(entity.getCurStatus(), String.valueOf(INT_TOW), String::equals)) {
            throw new TackBackException(USER_STATUS_DISABLE);
        }

        AppUserEntity appUserEntity = new AppUserEntity();
        appUserEntity.setUserId(reqVo.getUserId());
        checkExist(userEntity, appUserEntity);

        appUserMapper.updateById(userEntity);

        AppUserInfoEntity infoEntity = new AppUserInfoEntity();
        if (Integer.parseInt(userEntity.getCurStatus()) > NEG_ZERO) {
            updateNoDistribute(reqVo, userEntity, infoEntity);
        }
        if (!entity.getCityId().equals(userEntity.getCityId())) {
            updateNoDistribute(reqVo, userEntity, infoEntity);
            infoEntity.setAddress(CLEAR);
        }
        if (Objects.nonNull(infoEntity.getId())) {
            appUserInfoMapper.updateSelectById(infoEntity);
        }

        userEntity.setId(entity.getId());
        userEntity.setUserId(entity.getUserId());
        syncAppUser(dbRenYunSwitch, cityCache, dictCache, employCompanyMapper, msgService, userEntity, null, null, reqVo.getHandleUser());
    }

    static void checkSupplier(AppUserEntity userEntity, CommonService commonService, Integer handleUserNo) {
        SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(String.valueOf(handleUserNo));
        if (Objects.nonNull(accountEntity)) {
            boolean is = objEquals(accountEntity.getCompanyId(), userEntity.getEmploymentCompanyId(), Integer::equals) &&
                    objEquals(getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt), userEntity.getCityId(), List::contains);
            if (!is) {
                throw new TackBackException(OPERATE_NO_PERMIT);
            }
        }
    }

    private void updateNoDistribute(AppUserUpdateReqVo updateReqVo, AppUserEntity userEntity, AppUserInfoEntity infoEntity) {
        AppUserInfoEntity userInfoEntity = appUserInfoMapper.selectByUserId(userEntity.getId());
        infoEntity.setId(userInfoEntity.getId());
        infoEntity.setDistributable(NEG_ZERO);
        infoEntity.setUpdateOp(updateReqVo.getHandleUser());
    }

    @Transactional
    public void addAppUser(AppUserAddReqVo reqVo) throws Exception {
        if (reqVo.getStatusKey() == INT_TOW) {
            throw new TackBackException(USER_STATUS_DISABLE_NOT_ADD);
        }

        SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(String.valueOf(reqVo.getHandleUserNo()));
        if (Objects.nonNull(accountEntity)) {
            boolean is = objEquals(accountEntity.getCompanyId(), reqVo.getCompanyId(), Integer::equals) &&
                    objEquals(getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt), reqVo.getCityId(), List::contains);
            if (!is) {
                throw new TackBackException(NOT_ALLOWED_ADD_CITY);
            }
            reqVo.setCompanyId(accountEntity.getCompanyId());
        }
        AppUserEntity userEntity = addConvert(reqVo);

        AppUserEntity appUserEntity = new AppUserEntity();
        checkExist(userEntity, appUserEntity);
        appUserMapper.insertSelective(userEntity);
        AppUserInfoEntity entity = new AppUserInfoEntity();
        entity.setUserId(userEntity.getId());
        entity.setDistributable(Optional.ofNullable(userEntity.getCurStatus())
                .filter(status -> objEquals(status, String.valueOf(NEG_ZERO), String::equals))
                .map(status -> INT_ONE)
                .orElse(NEG_ZERO));
        entity.setCreateOp(reqVo.getHandleUser());
        appUserInfoMapper.insertSelective(entity);
        if (!objEquals(userEntity.getCurStatus(), String.valueOf(INT_TOW), String::equals)) {
            attendanceService.batchInsertAllUserThirtyAttendance(reqVo.getHandleUser(), userEntity);
        }
        syncAppUser(dbRenYunSwitch, cityCache, dictCache, employCompanyMapper, msgService, userEntity, null, null, reqVo.getHandleUser());
    }

    public int setUserPassWord(AppUserSetPassWordReqVo reqVo) throws Exception {
        AppUserEntity userEntity = setPassWordConvert(reqVo);
        AppUserEntity user = appUserMapper.selectByPrimaryKey(reqVo.getUserId());
        if (objEquals(user.getCurStatus(), String.valueOf(INT_TOW), String::equals)) {
            return NEG_FOUR;
        }

        SupplierAccountEntity accountEntity = commonService.getSupplierAccountByUserId(String.valueOf(reqVo.getHandleUserNo()));
        if (Objects.nonNull(accountEntity)) {
            boolean is = objEquals(accountEntity.getCompanyId(), user.getEmploymentCompanyId(), Integer::equals) &&
                    objEquals(getList(accountEntity.getMannageCity(), SPLIT_COMMA, Integer::parseInt), user.getCityId(), List::contains);
            if (!is) {
                return NEG_ZERO;
            }
        }
        int count = appUserMapper.updateById(userEntity);
        userEntity.setId(user.getId());
        userEntity.setUserId(user.getUserId());
        syncAppUser(dbRenYunSwitch, cityCache, dictCache, employCompanyMapper, msgService, userEntity, null, null, reqVo.getHandleUser());
        return count;
    }

    private void checkExist(AppUserEntity userEntityReq, AppUserEntity appUserEntity) {
        appUserEntity.setUserName(userEntityReq.getUserName());
        int nameCount  = appUserMapper.countByCond(appUserEntity);
        if (nameCount > 0L) {
            logger.info("***RESP*** 姓名已存在");
            throw new TackBackException(USER_NAME_EXIST);
        }
        appUserEntity.setUserName(null);
        appUserEntity.setMobile(userEntityReq.getMobile());
        int countMobile  = appUserMapper.countByCond(appUserEntity);
        if (countMobile > 0L) {
            logger.info("***RESP*** 手机已存在");
            throw new TackBackException(USER_MOBILE_EXIST);
        }
    }

    @Override
    public int importObj(Workbook book, String operator) throws Exception {
        return 0;
    }
}

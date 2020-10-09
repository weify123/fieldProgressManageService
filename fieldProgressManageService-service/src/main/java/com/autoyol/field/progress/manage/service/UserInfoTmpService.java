package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.entity.UserInfoTmpEntity;
import com.autoyol.field.progress.manage.mapper.UserInfoTmpMapper;
import com.autoyol.field.progress.manage.page.BasePage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.util.OprUtil.getStationCodeStr;

@Service
public class UserInfoTmpService {

    @Resource
    UserInfoTmpMapper userInfoTmpMapper;
    @Resource
    DictCache dictCache;

    public void set(BasePage basePage) throws Exception {
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache("station_type");
        List<UserInfoTmpEntity> entityList = userInfoTmpMapper.queryList(basePage);
        if (CollectionUtils.isNotEmpty(entityList)) {
            entityList = entityList.stream()
                    .peek(tmp -> tmp.setStation(getStationCodeStr(tmp.getStation(), dictList)))
                    .filter(tmp -> Objects.nonNull(tmp.getStation()))
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isNotEmpty(entityList)) {
            userInfoTmpMapper.batchUpdate(entityList);
        }

    }
}
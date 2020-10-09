package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.entity.VersionConfEntity;
import com.autoyol.field.progress.manage.mapper.VersionConfMapper;
import com.autoyol.field.progress.manage.request.version.VersionAddReqVo;
import com.autoyol.field.progress.manage.request.version.VersionPageReqVo;
import com.autoyol.field.progress.manage.request.version.VersionUpdateReqVo;
import com.autoyol.field.progress.manage.response.version.VersionMaxRespVo;
import com.autoyol.field.progress.manage.response.version.VersionVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class VersionService {

    @Resource
    VersionConfMapper versionConfMapper;

    @Resource
    DictCache dictCache;

    public List<VersionVo> findPageByCond(VersionPageReqVo reqVo) throws Exception {
        VersionConfEntity entity = new VersionConfEntity();
        entity.setInnerVersion(reqVo.getInnerVersion());
        entity.setOuterVersion(reqVo.getOuterVersion());
        entity.setPublished(reqVo.getPublishedKey());
        List<VersionConfEntity> entities = versionConfMapper.findByCond(entity);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.EMPTY_LIST;
        }
        List<String> typeList = buildTypeList(PUBLISH_TYPE, PLATFORM_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return entities.stream().sorted(Comparator.comparing(VersionConfEntity::getInnerVersion).reversed()).map(version -> buildVersionVo(map, version)).collect(Collectors.toList());
    }

    public VersionVo findVersionById(Integer id) throws Exception {
        VersionConfEntity entity = versionConfMapper.selectById(id);
        if (Objects.isNull(entity)) {
            return new VersionVo();
        }
        List<String> typeList = buildTypeList(PUBLISH_TYPE, PLATFORM_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return buildVersionVo(map, entity);
    }

    public int add(VersionAddReqVo reqVo) {
        VersionConfEntity entity = new VersionConfEntity();
        BeanUtils.copyProperties(reqVo, entity);
        entity.setPlatformType(reqVo.getPlatformTypeKey());
        entity.setForceUpdate(reqVo.getForceUpdateKey());
        entity.setHomeNoticed(reqVo.getHomeNoticedKey());
        entity.setPublished(reqVo.getPublishedKey());

        Integer x = isExistVersion(entity);
        if (x != null) {
            return x;
        }

        return versionConfMapper.insertSelective(entity);
    }

    public int update(VersionUpdateReqVo reqVo) {
        VersionConfEntity entity = new VersionConfEntity();
        BeanUtils.copyProperties(reqVo, entity);
        entity.setHomeNoticed(reqVo.getHomeNoticedKey());
        entity.setPublished(reqVo.getPublishedKey());

        Integer x = isExistVersion(entity);
        if (x != null) {
            return x;
        }

        entity.setForceUpdate(null);
        entity.setPlatformType(null);
        return versionConfMapper.updateById(entity);
    }

    private Integer isExistVersion(VersionConfEntity entity) {
        VersionConfEntity query = new VersionConfEntity();
        query.setId(entity.getId());
        query.setInnerVersion(entity.getInnerVersion());
        int count = versionConfMapper.countByCond(query);

        if (count > NEG_ZERO) {
            return NEG_ONE;
        }
        query.setInnerVersion(null);
        query.setOuterVersion(entity.getOuterVersion());
        count = versionConfMapper.countByCond(query);
        if (count > NEG_ZERO) {
            return NEG_TOW;
        }
        return null;
    }

    public int delete(Integer id) {
        VersionConfEntity entity = new VersionConfEntity();
        entity.setId(id);
        entity.setIsDelete(INT_ONE);
        return versionConfMapper.updateById(entity);
    }

    public VersionMaxRespVo getMaxVersion() {
        VersionMaxRespVo respVo = new VersionMaxRespVo();
        List<VersionConfEntity> versionList = versionConfMapper.findByCond(null);
        if (CollectionUtils.isEmpty(versionList)) {
            return respVo;
        }
        VersionConfEntity innerEntity = versionList.stream().max((v1, v2) -> getRet(v1.getInnerVersion().split(SPLIT_POINT), v2.getInnerVersion().split(SPLIT_POINT)))
                .orElse(new VersionConfEntity());
        VersionConfEntity outerEntity = versionList.stream().max((v1, v2) -> getRet(v1.getOuterVersion().split(SPLIT_POINT), v2.getOuterVersion().split(SPLIT_POINT)))
                .orElse(new VersionConfEntity());
        respVo.setInnerMaxVersion(Optional.of(innerEntity).map(VersionConfEntity::getInnerVersion).orElse(STRING_EMPTY));
        respVo.setOuterMaxVersion(Optional.of(outerEntity).map(VersionConfEntity::getOuterVersion).orElse(STRING_EMPTY));
        return respVo;
    }

    private VersionVo buildVersionVo(Map<String, List<SysDictEntity>> map, VersionConfEntity version) {
        VersionVo vo = new VersionVo();
        BeanUtils.copyProperties(version, vo);
        vo.setPlatformTypeKey(version.getPlatformType());
        vo.setPlatformTypeVal(getLabelByCode(map.get(PLATFORM_TYPE), String.valueOf(version.getPlatformType())));
        vo.setForceUpdateKey(version.getForceUpdate());
        vo.setForceUpdateVal(getLabelByCode(map.get(PUBLISH_TYPE), String.valueOf(version.getForceUpdate())));
        vo.setHomeNoticedKey(version.getHomeNoticed());
        vo.setHomeNoticedVal(getLabelByCode(map.get(PUBLISH_TYPE), String.valueOf(version.getHomeNoticed())));
        vo.setPublishedKey(version.getPublished());
        vo.setPublishedVal(getLabelByCode(map.get(PUBLISH_TYPE), String.valueOf(version.getPublished())));
        return vo;
    }
}

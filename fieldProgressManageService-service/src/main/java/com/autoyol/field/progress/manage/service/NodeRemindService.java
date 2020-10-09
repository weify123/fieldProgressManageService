package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.NodeRemindConfEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.mapper.NodeRemindConfMapper;
import com.autoyol.field.progress.manage.request.node.RemindAddReqVo;
import com.autoyol.field.progress.manage.request.node.RemindDeleteReaVo;
import com.autoyol.field.progress.manage.request.node.RemindPageReqVo;
import com.autoyol.field.progress.manage.request.node.RemindUpdateReqVo;
import com.autoyol.field.progress.manage.response.node.RemindVo;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.LocalDateUtil.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class NodeRemindService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    NodeRemindConfMapper nodeRemindConfMapper;

    @Resource
    DictCache dictCache;

    public List<RemindVo> findPageByCond(RemindPageReqVo reqVo) throws Exception {
        NodeRemindConfEntity entity = getSearchNodeRemindConfEntity(reqVo);
        List<NodeRemindConfEntity> entities = nodeRemindConfMapper.findByCond(entity);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.EMPTY_LIST;
        }
        List<String> typeList = buildTypeList(FLOW_NAME_TYPE, FLOW_SERVER_TYPE, FLOW_NODE_NAME_TYPE, EFFECTIVE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return entities.stream().sorted(Comparator.comparing(NodeRemindConfEntity::getNoticeStartTime).reversed()).map(confEntity -> convertEntityToRemindVo(map, confEntity)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public RemindVo findRemindById(Integer id) throws Exception {
        NodeRemindConfEntity entity = nodeRemindConfMapper.selectById(id);
        if (Objects.isNull(entity)) {
            return new RemindVo();
        }
        List<String> typeList = buildTypeList(FLOW_NAME_TYPE, FLOW_SERVER_TYPE, FLOW_NODE_NAME_TYPE, EFFECTIVE_TYPE);
        Map<String, List<SysDictEntity>> map = dictCache.getDictByTypeNameStrFromCache(typeList);
        return convertEntityToRemindVo(map, entity);
    }

    public int add(RemindAddReqVo reqVo) throws Exception {
        NodeRemindConfEntity entity = new NodeRemindConfEntity();
        ConvertBeanUtil.copyProperties(entity, reqVo);
        entity.setFlowServerType(reqVo.getServerTypeKey());
        entity.setCreateOp(reqVo.getHandleUser());
        entity.setNoticeStartTime(convertToDate(convertToLocalDateTime(convertToDate(reqVo.getNoticeStartTime()))));
        entity.setNoticeEndTime(convertToDate(convertToLocalDateTime(convertToDate(reqVo.getNoticeEndTime()))
                .withHour(INT_TWENTY_THREE).withMinute(INT_FIFTY_NINE).withSecond(INT_FIFTY_NINE)));
        int count = nodeRemindConfMapper.countExist(entity);
        if (count > NEG_ZERO) {
            return NEG_ONE;
        }
        return nodeRemindConfMapper.insertSelective(entity);
    }

    public int update(RemindUpdateReqVo reqVo) throws Exception {
        NodeRemindConfEntity entity = new NodeRemindConfEntity();
        ConvertBeanUtil.copyProperties(entity, reqVo);
        entity.setUpdateOp(reqVo.getHandleUser());
        entity.setNoticeStartTime(convertToDate(convertToLocalDateTime(convertToDate(reqVo.getNoticeStartTime()))));
        entity.setNoticeEndTime(convertToDate(convertToLocalDateTime(convertToDate(reqVo.getNoticeEndTime()))
                .withHour(INT_TWENTY_THREE).withMinute(INT_FIFTY_NINE).withSecond(INT_FIFTY_NINE)));

        NodeRemindConfEntity confEntity = nodeRemindConfMapper.selectById(reqVo.getId());
        entity.setFlowNameKey(confEntity.getFlowNameKey());
        entity.setFlowServerType(confEntity.getFlowServerType());
        entity.setFlowNodeNameKey(confEntity.getFlowNodeNameKey());

        int count = nodeRemindConfMapper.countExist(entity);
        if (count > NEG_ZERO) {
            return NEG_ONE;
        }
        return nodeRemindConfMapper.updateById(entity);
    }

    public int deleteById(RemindDeleteReaVo reqVo) {
        NodeRemindConfEntity entity = new NodeRemindConfEntity();
        entity.setId(reqVo.getId());
        entity.setIsDelete(INT_ONE);
        return nodeRemindConfMapper.updateById(entity);
    }

    private RemindVo convertEntityToRemindVo(Map<String, List<SysDictEntity>> map, NodeRemindConfEntity confEntity) {
        RemindVo vo = new RemindVo();
        try {
            ConvertBeanUtil.copyProperties(vo, confEntity);
            vo.setFlowNameKey(confEntity.getFlowNameKey());
            vo.setFlowNameVal(getLabelByCode(map.get(FLOW_NAME_TYPE), String.valueOf(confEntity.getFlowNameKey())));
            vo.setServerTypeKey(confEntity.getFlowServerType());
            vo.setServerTypeVal(getLabelByCode(map.get(FLOW_SERVER_TYPE), String.valueOf(confEntity.getFlowServerType())));
            vo.setFlowNodeNameKey(confEntity.getFlowNodeNameKey());
            vo.setFlowNodeNameVal(getLabelByCode(map.get(FLOW_NODE_NAME_TYPE), String.valueOf(confEntity.getFlowNodeNameKey())));

            LocalDate end = convertToLocalDate(confEntity.getNoticeEndTime());
            LocalDate now = LocalDate.now();
            vo.setEffectivedVal(Optional.ofNullable(end).filter(e -> e.isEqual(now) || e.isAfter(now))
                    .map(e -> {
                        vo.setEffectivedKey(INT_ONE);
                        return getLabel1ByCode(map.get(EFFECTIVE_TYPE), String.valueOf(INT_ONE));
                    })
                    .orElseGet(() -> {
                        vo.setEffectivedKey(NEG_ZERO);
                        return getLabel1ByCode(map.get(EFFECTIVE_TYPE), String.valueOf(NEG_ZERO));
                    }));
            return vo;
        } catch (Exception e) {
            logger.error("***PRO*** NodeRemindService findPageByCond copyProperties e ={}", e);
            return null;
        }
    }

    private NodeRemindConfEntity getSearchNodeRemindConfEntity(RemindPageReqVo reqVo) {
        NodeRemindConfEntity entity = new NodeRemindConfEntity();
        if (Objects.nonNull(reqVo.getNoticeStartTime())) {
            entity.setNoticeStartTime(convertToDate(convertToLocalDateTime(reqVo.getNoticeStartTime())));
        }
        if (Objects.nonNull(reqVo.getNoticeEndTime())) {
            entity.setNoticeEndTime(convertToDate(convertToLocalDateTime(reqVo.getNoticeEndTime())
                    .withHour(INT_TWENTY_THREE).withMinute(INT_FIFTY_NINE).withSecond(INT_FIFTY_NINE)));
        }
        entity.setFlowNameKey(Optional.ofNullable(reqVo.getFlowNameKey()).orElse(null));
        entity.setFlowServerType(Optional.ofNullable(reqVo.getServerTypeKey()).orElse(null));
        entity.setFlowNodeNameKey(Optional.ofNullable(reqVo.getFlowNodeNameKey()).orElse(null));
        entity.setEffectived(Optional.ofNullable(reqVo.getEffectivedKey()).orElse(null));
        return entity;
    }
}

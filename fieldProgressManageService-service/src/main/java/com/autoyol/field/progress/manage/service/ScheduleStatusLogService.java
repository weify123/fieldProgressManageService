package com.autoyol.field.progress.manage.service;

import com.autoyol.field.progress.manage.cache.DictCache;
import com.autoyol.field.progress.manage.entity.PickDeliverScheduleStatusLogEntity;
import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.mapper.PickDeliverScheduleStatusLogMapper;
import com.autoyol.field.progress.manage.response.schedulelog.ScheduleLogVo;
import com.autoyol.field.progress.manage.util.ConvertBeanUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.*;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Service
public class ScheduleStatusLogService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    PickDeliverScheduleStatusLogMapper pickDeliverScheduleStatusLogMapper;

    @Resource
    private DictCache dictCache;

    public List<ScheduleLogVo> queryObjListByOrderId(String orderId) throws Exception{
        List<PickDeliverScheduleStatusLogEntity> entityList = pickDeliverScheduleStatusLogMapper.queryObjListByOrderId(orderId);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.EMPTY_LIST;
        }
        List<SysDictEntity> dictList = dictCache.getDictByTypeNameFromCache(PICK_DELIVER_SCHEDULE_STATUS);
        return entityList.stream().map(entity -> {
            ScheduleLogVo vo = new ScheduleLogVo();
            try {
                ConvertBeanUtil.copyProperties(vo, entity);
                vo.setScheduleStatusKey(entity.getScheduleStatus());
                vo.setScheduleStatusVal(getLabelByCode(dictList, String.valueOf(entity.getScheduleStatus())));
                return vo;
            } catch (Exception e) {
                logger.error("复制错误,{}", e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}

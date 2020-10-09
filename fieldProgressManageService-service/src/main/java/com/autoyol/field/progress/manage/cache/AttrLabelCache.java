package com.autoyol.field.progress.manage.cache;

import com.autoyol.field.progress.manage.entity.AttrLabelEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.AttrLabelMapper;
import com.autoyol.field.progress.manage.util.OprUtil;
import com.dianping.cat.Cat;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_FIVE;
import static com.autoyol.field.progress.manage.cache.CacheConstraint.SPLIT_COMMA;
import static com.autoyol.field.progress.manage.util.OprUtil.*;

@Component
public class AttrLabelCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    AttrLabelMapper attrLabelMapper;

    @Resource
    private RedisTemplate redisTemplate;

    public void deleteAttrLabelCache(Object... ids) {
        redisTemplate.opsForHash().delete(CacheConstraint.ATTR_LABEL_KEY, ids);
    }

    public void addAttrToCache(Integer id, AttrLabelEntity attrLabelEntity) {
        redisTemplate.opsForHash().put(CacheConstraint.ATTR_LABEL_KEY, String.valueOf(id), attrLabelEntity);
    }

    public List<AttrLabelEntity> getAllAttrListFromCache() {
        return (List<AttrLabelEntity>) redisTemplate.opsForHash().values(CacheConstraint.ATTR_LABEL_KEY);
    }

    @HystrixCommand(fallbackMethod = "getAttrListByBelongSysError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public List<AttrLabelEntity> getAttrListByBelongSysFromCache(int belong) throws Exception{
        List<AttrLabelEntity> list = (List<AttrLabelEntity>) redisTemplate.opsForHash().values(CacheConstraint.ATTR_LABEL_KEY);
        if (CollectionUtils.isEmpty(list)) {
            logger.info("***PRO*** getAttrListByBelongSysFromCache redis缓存不存在, 准备走降级,belong=[{}]", belong);
            Cat.logEvent("attrDown", "getAttrListByBelongSysFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        return list.stream()
                .filter(attr -> hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), attr.getIsDelete(), Integer::equals))
                .filter(attr -> hitListSearch(getList(attr.getBelongService(), SPLIT_COMMA, Integer::parseInt), belong, List::contains))
                .collect(Collectors.toList());
    }

    private List<AttrLabelEntity> getAttrListByBelongSysError(int belong) {
        logger.error("***PRO*** getAttrListByBelongSysFromCache 走熔断");
        Cat.logEvent("allAttrDown", "getAttrListByBelongSysError 走熔断降级");
        List<AttrLabelEntity> entities = attrLabelMapper.selectAll();
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }

        try {
            entities.forEach(attrLabelEntity -> {
                addAttrToCache(attrLabelEntity.getId(), attrLabelEntity);
            });
            redisTemplate.expire(CacheConstraint.ATTR_LABEL_KEY, INT_FIVE, TimeUnit.DAYS);
        } catch (Exception e) {
            logger.error("***PRO*** 回写redis失败,belong=[{}]，error:{}", belong, e);
            Cat.logError("getAttrListByBelongSysError 回写redis失败 [{}]", e);
        }
        return entities.stream()
                .filter(attr -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), attr.getIsDelete(), Integer::equals))
                .filter(attr -> hitListSearch(getList(attr.getBelongService(), SPLIT_COMMA, Integer::parseInt), belong, List::contains))
                .collect(Collectors.toList());
    }
}

package com.autoyol.field.progress.manage.cache;

import com.autoyol.field.progress.manage.entity.SysDictEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.SysDictEntityMapper;
import com.autoyol.field.progress.manage.util.OprUtil;
import com.dianping.cat.Cat;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_FIVE;

@Component
public class DictCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SysDictEntityMapper sysDictEntityMapper;

    public void addDictToCache(String name, List<SysDictEntity> list) {
        redisTemplate.opsForHash().put(CacheConstraint.DICT_KEY, String.valueOf(name), list);
        redisTemplate.expire(CacheConstraint.DICT_KEY, INT_FIVE, TimeUnit.DAYS);
    }

    public void addDictMapToCache(Map<String, List<SysDictEntity>> map) {
        redisTemplate.opsForHash().putAll(CacheConstraint.DICT_KEY, map);
        redisTemplate.expire(CacheConstraint.DICT_KEY, INT_FIVE, TimeUnit.DAYS);
    }

    @HystrixCommand(fallbackMethod = "getDictByTypeNameError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public List<SysDictEntity> getDictByTypeNameFromCache(String typeName) throws Exception {

        List<SysDictEntity> list = (List<SysDictEntity>) redisTemplate.opsForHash().get(CacheConstraint.DICT_KEY, typeName);
        if (CollectionUtils.isEmpty(list)) {
            logger.info("***PRO*** getDictByTypeNameFromCache redis缓存不存在, 准备走降级,typeName=[{}]", typeName);
            Cat.logEvent("dictDown", "getDictByTypeNameFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        return list.stream()
                .filter(dict -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), dict.getIsDelete(), Integer::equals))
                .collect(Collectors.toList());
    }

    private List<SysDictEntity> getDictByTypeNameError(String typeName) {
        logger.info("***PRO*** getDictByTypeNameFromCache 走熔断 typeName [{}]", typeName);
        List<SysDictEntity> list = sysDictEntityMapper.selectByTypeName(typeName);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        try {
            this.addDictToCache(typeName, list);
        } catch (Exception e) {
            logger.error("***PRO*** 回写redis失败,typeName=[{}]，error:{}", typeName, e);
            Cat.logError("getDictByTypeNameError 回写redis失败 {}", e);
        }
        return Optional.of(list)
                .map(ls -> ls.stream()
                        .filter(dict -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), dict.getIsDelete(), Integer::equals))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @HystrixCommand(fallbackMethod = "getDictByTypeNameStrError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public Map<String, List<SysDictEntity>> getDictByTypeNameStrFromCache(List<String> typeList) throws Exception {

        List<List<SysDictEntity>> listCol = redisTemplate.opsForHash().multiGet(CacheConstraint.DICT_KEY, typeList);
        if (CollectionUtils.isEmpty(listCol)) {
            logger.info("***PRO*** getDictByTypeNameStrFromCache redis缓存不存在, 准备走降级,typeList=[{}]", typeList);
            Cat.logEvent("dictDown", "getDictByTypeNameStrFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        List<SysDictEntity> list = listCol.stream().collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        List<String> strings1 = list.stream().map(SysDictEntity::getTypeName).distinct().collect(Collectors.toList());
        typeList.forEach(inputName -> {
            if (!strings1.contains(inputName)) {
                try {
                    List<SysDictEntity> entities = getDictByTypeNameFromCache(inputName);
                    if (CollectionUtils.isEmpty(entities)) {
                        return;
                    }
                    list.addAll(entities);
                } catch (Exception e) {
                    logger.error("***PRO*** 批量查询字典转单条 查询异常,{}", e);
                    Cat.logError("批量查询字典转单条 查询异常 {}", e);
                }
            }
        });
        return list.stream()
                .filter(dict -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), dict.getIsDelete(), Integer::equals))
                .collect(Collectors.groupingBy(SysDictEntity::getTypeName));
    }

    private Map<String, List<SysDictEntity>> getDictByTypeNameStrError(List<String> typeList) {
        logger.error("***PRO*** getDictByTypeNameStrFromCache 走熔断 typeName [{}]", typeList);
        List<SysDictEntity> list = sysDictEntityMapper.selectByTypeNameStr(typeList);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        try {
            this.addDictMapToCache(list.stream().collect(Collectors.groupingBy(SysDictEntity::getTypeName)));
        } catch (Exception e) {
            logger.error("***PRO*** 回写redis失败,typeList=[{}]，error:{}", typeList, e);
            Cat.logError("getDictByTypeNameError 回写redis失败 [{}]", e);
        }
        return Optional.of(list)
                .map(ls -> ls.stream()
                        .filter(dict -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), dict.getIsDelete(), Integer::equals))
                        .collect(Collectors.groupingBy(SysDictEntity::getTypeName)))
                .orElse(null);
    }

    public void deleteDictCache(Object... typeNames) {
        redisTemplate.opsForHash().delete(CacheConstraint.DICT_KEY, typeNames);
    }

    @HystrixCommand(fallbackMethod = "getAllDictError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public List<SysDictEntity> getAllDictFromCache() throws Exception {
        List<SysDictEntity> objects = redisTemplate.opsForHash().values(CacheConstraint.DICT_KEY);
        if (CollectionUtils.isEmpty(objects)) {
            logger.info("***PRO*** getAllDictFromCache redis缓存不存在, 准备走降级");
            Cat.logEvent("AllDictDown", "getAllDictFromCache 准备走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        return objects.stream()
                .filter(dict -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), dict.getIsDelete(), Integer::equals))
                .collect(Collectors.toList());
    }

    private List<SysDictEntity> getAllDictError() {
        logger.error("***PRO*** getAllDictFromCache 走熔断");
        Cat.logEvent("AllDictDown", "getAllDictError 走熔断降级");
        List<SysDictEntity> entities = sysDictEntityMapper.selectAll();
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }
        return entities.stream()
                .filter(dict -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), dict.getIsDelete(), Integer::equals))
                .collect(Collectors.toList());
    }
}

package com.autoyol.field.progress.manage.cache;

import com.autoyol.field.progress.manage.entity.CityLevelEntity;
import com.autoyol.field.progress.manage.enums.CommonEnum;
import com.autoyol.field.progress.manage.mapper.CityLevelMapper;
import com.autoyol.field.progress.manage.util.OprUtil;
import com.dianping.cat.Cat;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_FIVE;

@Component
public class CityCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CityLevelMapper cityLevelMapper;

    public void addCityToCache(CityLevelEntity entity) {
        redisTemplate.opsForHash().put(CacheConstraint.CITY_KEY, String.valueOf(entity.getCityId()), entity);
        redisTemplate.expire(CacheConstraint.CITY_KEY, INT_FIVE, TimeUnit.DAYS);
    }

    @HystrixCommand(fallbackMethod = "getCityError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public CityLevelEntity getCityByIdFromCache(Integer cityId) throws Exception {
        CityLevelEntity o = (CityLevelEntity) redisTemplate.opsForHash().get(CacheConstraint.CITY_KEY, String.valueOf(cityId));
        if (Objects.isNull(o)) {
            logger.info("***PRO*** getCityByIdFromCache redis缓存不存在, 准备走降级,cityId=[{}]", cityId);
            Cat.logEvent("cityDown", "getAppUserByIdFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        CityLevelEntity entity = Optional.ofNullable(o)
                .filter(city -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), city.getIsDelete(), Integer::equals))
                .orElse(null);
        if (Objects.isNull(entity)) {
            logger.info("***PRO*** getCityByIdFromCache redis缓存不存在, 准备走降级,entity");
            Cat.logEvent("cityDown", "getAppUserByIdFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        return entity;
    }

    private CityLevelEntity getCityError(Integer cityId) {
        logger.error("***PRO*** getCityByIdFromCache 走熔断 cityId [{}]", cityId);
        CityLevelEntity entity = cityLevelMapper.selectByPrimaryKey(cityId);
        if (Objects.isNull(entity)) {
            return null;
        }
        try {
            this.addCityToCache(entity);
        } catch (Exception e) {
            logger.error("***PRO*** 回写redis失败,cityId=[{}]，error:{}", cityId, e);
            Cat.logError("getCityError 回写redis失败 [{}]", e);
        }
        return Optional.ofNullable(entity)
                .filter(city -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), city.getIsDelete(), Integer::equals))
                .orElse(null);
    }

    @HystrixCommand(fallbackMethod = "getCityByIdListError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public List<CityLevelEntity> getCityByIdListFromCache(List<Integer> cityIdList) throws Exception {
        List<List<CityLevelEntity>> listCol = redisTemplate.opsForHash().multiGet(CacheConstraint.CITY_KEY, cityIdList);
        List<CityLevelEntity> cityEntityList = listCol.stream().collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        if (CollectionUtils.isEmpty(cityEntityList)) {
            logger.info("***PRO*** getCityByIdListFromCache redis缓存不存在, 准备走降级,cityIdList=[{}]", cityIdList);
            Cat.logEvent("cityDown", "getCityByIdListFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }

        if (cityEntityList.size() < cityIdList.size()) {
            logger.info("***PRO*** getCityByIdListFromCache redis缓存数据不全, 准备走降级,entity");
            Cat.logEvent("cityDown", "getCityByIdListFromCache 缓存数据不全 走熔断降级");
            throw new RuntimeException("redis缓存缓存数据不全");
        }
        return cityEntityList.stream()
                .filter(city -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), city.getIsDelete(), Integer::equals))
                .collect(Collectors.toList());
    }

    private List<CityLevelEntity> getCityByIdListError(List<Integer> cityIdList) {
        logger.error("***PRO*** getCityByIdListFromCache 走熔断 cityIdList [{}]", cityIdList);
        List<CityLevelEntity> cityEntityList = cityLevelMapper.selectByCityIdList(cityIdList);
        if (CollectionUtils.isEmpty(cityEntityList)) {
            return null;
        }
        try {
            cityEntityList.forEach(this::addCityToCache);
        } catch (Exception e) {
            logger.error("***PRO*** 回写redis失败,cityIdList=[{}]，error:{}", cityIdList, e);
            Cat.logError("getCityByIdListError 回写redis失败 [{}]", e);
        }
        return cityEntityList.stream()
                .filter(city -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), city.getIsDelete(), Integer::equals))
                .collect(Collectors.toList());
    }

    public void deleteCityCache(Object... cityIds) {
        redisTemplate.opsForHash().delete(CacheConstraint.CITY_KEY, cityIds);
    }

    @HystrixCommand(fallbackMethod = "getAllCityError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public List<CityLevelEntity> getAllCityFromCache() throws Exception {
        List<CityLevelEntity> objects = redisTemplate.opsForHash().values(CacheConstraint.CITY_KEY);
        if (CollectionUtils.isEmpty(objects)) {
            logger.info("***PRO*** getAllCityFromCache redis缓存不存在, 准备走降级");
            Cat.logEvent("AllCityDown", "getAllCityFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        return objects.stream()
                .filter(city -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), city.getIsDelete(), Integer::equals))
                .collect(Collectors.toList());
    }

    private List<CityLevelEntity> getAllCityError() {
        logger.error("***PRO*** getAllCityFromCache 走熔断");
        Cat.logEvent("AllCityDown", "getAllCityError 走熔断降级");
        List<CityLevelEntity> entities = cityLevelMapper.selectAll();
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        }
        return entities.stream()
                .filter(city -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), city.getIsDelete(), Integer::equals))
                .peek(this::addCityToCache)
                .collect(Collectors.toList());
    }


    @HystrixCommand(fallbackMethod = "getCityIdByCityNameError", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public Integer getCityIdByCityNameFromCache(String cityName) throws Exception {
        List<CityLevelEntity> objects = redisTemplate.opsForHash().values(CacheConstraint.CITY_KEY);
        if (CollectionUtils.isEmpty(objects)) {
            logger.info("***PRO*** getAllCityIdByCityNameFromCache redis缓存不存在, 准备走降级");
            Cat.logEvent("getCityIdDown", "getCityIdFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        int cityId = objects.stream()
                .filter(city -> OprUtil.hitSearch(Integer.parseInt(CommonEnum.NOT_DELETE.getCode()), city.getIsDelete(), Integer::equals))
                .filter(city -> OprUtil.objEquals(city.getCity(), cityName.trim(), String::equals))
                .map(CityLevelEntity::getCityId)
                .findFirst().orElse(CacheConstraint.NEG_ZERO);
        if (cityId <= CacheConstraint.NEG_ZERO) {
            logger.info("***PRO*** getAllCityIdByCityNameFromCache redis缓存不存在, 准备走降级");
            Cat.logEvent("getCityIdDown", "getCityIdFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        return cityId;
    }

    private Integer getCityIdByCityNameError(String cityName) {
        logger.error("***PRO*** getAllCityIdByCityNameFromCache 走熔断");
        Cat.logEvent("getCityIdDown", "getCityIdByCityNameError 走熔断降级");

        Integer cityId = CacheConstraint.NEG_ONE;
        try {
            cityId = cityLevelMapper.getCityIdByName(cityName.trim());
            if (Objects.isNull(cityId) || cityId < CacheConstraint.NEG_ZERO) {
                return CacheConstraint.NEG_ONE;
            }
            this.addCityToCache(cityLevelMapper.selectByPrimaryKey(cityId));
        } catch (Exception e) {
            logger.error("***PRO*** getCityIdByCityNameError 回写redis失败,cityId=[{}]，error:{}", cityId, e);
            Cat.logError("getCityIdByCityNameError 回写redis失败 [{}]", e);
        }
        return cityId;
    }
}

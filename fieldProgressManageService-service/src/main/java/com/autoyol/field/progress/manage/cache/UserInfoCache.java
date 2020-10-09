package com.autoyol.field.progress.manage.cache;

import com.autoyol.field.progress.manage.entity.AppUserInfoEntity;
import com.autoyol.field.progress.manage.mapper.AppUserInfoMapper;
import com.autoyol.field.progress.manage.vo.UserInfoVo;
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

@Component
public class UserInfoCache {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppUserInfoMapper appUserInfoMapper;

    public void addMobileAndIdNoToCache(String name, List<UserInfoVo> list) {
        redisTemplate.opsForValue().set(name, list);
        redisTemplate.expire(name, INT_FIVE, TimeUnit.DAYS);
    }

    public void deleteMobileAndIdNoCache() {
        redisTemplate.opsForValue().getOperations().delete(CacheConstraint.APP_USER_INFO_ID_NO_KEY);
    }

    @HystrixCommand(fallbackMethod = "findAllFailBack", groupKey = "RedisCacheGroup", threadPoolKey = "RedisCacheThread")
    public List<UserInfoVo> findAllMobileAndIdNoFromCache(){

        List<UserInfoVo> vos = (List<UserInfoVo>)redisTemplate.opsForValue().get(CacheConstraint.APP_USER_INFO_ID_NO_KEY);

        if (CollectionUtils.isEmpty(vos)) {
            logger.info("***PRO*** findAllMobileAndIdNoFromCache redis缓存不存在, 准备走降级");
            Cat.logEvent("dictDown", "findAllMobileAndIdNoFromCache 走熔断降级");
            throw new RuntimeException("redis缓存不存在");
        }
        return vos;
    }

    private List<UserInfoVo> findAllFailBack(){
        List<AppUserInfoEntity> list = appUserInfoMapper.queryAllMobileAndIdNo();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        try {
            this.addMobileAndIdNoToCache(CacheConstraint.APP_USER_INFO_ID_NO_KEY, buildCacheList(list));
        } catch (Exception e) {
            logger.error("***PRO*** findAllFailBack 回写redis失败，error:{}", e);
            Cat.logError("findAllFailBack 回写redis失败 {}", e);
        }
        return buildCacheList(list);
    }

    private List<UserInfoVo> buildCacheList(List<AppUserInfoEntity> list) {
        return list.stream().map(entity -> new UserInfoVo(entity.getUserId(), entity.getContactMobile(), entity.getIdNo()))
                .collect(Collectors.toList());
    }


}

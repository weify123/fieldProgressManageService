package com.autoyol.field.progress.manage.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static com.autoyol.field.progress.manage.cache.CacheConstraint.INT_ONE;

@Service
public class RedisOprService {

    @Resource
    private RedisTemplate redisTemplate;

    public long incrementAndGet(String key, Date expireDate) {
        RedisAtomicLong currentIncrLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        long increment = currentIncrLong.incrementAndGet();
        if (increment == INT_ONE) {
            currentIncrLong.expireAt(expireDate);
        }
        return increment;
    }

    public void decrement(String key) {
        RedisAtomicLong currentIncrLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        long current = currentIncrLong.get();
        if (current >= INT_ONE) {
            currentIncrLong.decrementAndGet();
        }
    }
}

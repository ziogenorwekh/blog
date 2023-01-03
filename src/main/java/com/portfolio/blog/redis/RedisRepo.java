package com.portfolio.blog.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepo {

    private final StringRedisTemplate stringRedisTemplate;

    public String findOne(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void save(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void saveRefresh(String key, String value) {
//        refresh 1days.
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        // 재발급 경우 가지고 있던 키 값 삭제
        if (valueOperations.get(key) != null) {
            log.info("key removed.");
            this.delete(key);
        }
        Duration expireDuration = Duration.ofDays(1L);
//        Duration expireDuration = Duration.ofSeconds(3L);

        valueOperations.set(key, value,expireDuration);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }
}

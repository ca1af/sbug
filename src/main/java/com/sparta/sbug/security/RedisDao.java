package com.sparta.sbug.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;

// springframework repository
@Repository
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 생성자
     *
     * @param redisTemplate 레디스 템플릿
     */
    public RedisDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 값을 저장
     *
     * @param key      키 (key)
     * @param data     값 (Value)
     * @param duration 수명
     */
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    /**
     * 키로 값을 조회
     *
     * @param key 키 (key)
     * @return String : 값
     */
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    /**
     * 키로 값을 삭제
     *
     * @param key 키 (key)
     */
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
    public void flushAll(){
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }
}
package com.sparta.sbug.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.*;

import java.time.Duration;


@Configuration
public class CacheConfiguration {

    @Bean
    public RedisCacheManager cacheManager(
        RedisConnectionFactory connectionFactory
    ) {

        RedisCacheConfiguration defaultConfig
            = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                    RedisSerializationContext
                    .SerializationPair
                    .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
/*                
        RedisCacheConfiguration elasticTtlconfig
            = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                    RedisSerializationContext
                    .SerializationPair
                    .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
*/

        // 리소스 유형에 따라 만료 시간을 다르게 지정
        Map<String, RedisCacheConfiguration> redisCacheConfigMap
            = new HashMap<>();
        redisCacheConfigMap.put(
            CacheNames.USER,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );
        redisCacheConfigMap.put(
            CacheNames.USERDETAILS,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );
        redisCacheConfigMap.put(
            CacheNames.ALLUSERS,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );
        redisCacheConfigMap.put(
            CacheNames.THREAD,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );
        redisCacheConfigMap.put(
            CacheNames.THREADSINCHANNEL,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );
        redisCacheConfigMap.put(
            CacheNames.COMMENT,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );
        redisCacheConfigMap.put(
            CacheNames.COMMENTSINTHREAD,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );
        redisCacheConfigMap.put(
            CacheNames.EMOJI,
            defaultConfig.entryTtl(Duration.ofHours(4))
        );

        RedisCacheManager redisCacheManager
            = RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(redisCacheConfigMap)
                .build();

			return redisCacheManager;
    }
}
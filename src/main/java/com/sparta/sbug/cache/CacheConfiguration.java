package com.sparta.sbug.cache;

import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class CacheConfiguration {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {


//        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
//                .builder()
//                .allowIfSubType(Object.class)
//                .build();


        RedisCacheConfiguration defaultConfig
                = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new JdkSerializationRedisSerializer())
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
                CacheNames.USERBYEMAIL,
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
                CacheNames.CHANNELS,
                defaultConfig.entryTtl(Duration.ofHours(4))
        );

        return RedisCacheManager.builder(connectionFactory)
        .withInitialCacheConfigurations(redisCacheConfigMap)
        .build();
    }

}

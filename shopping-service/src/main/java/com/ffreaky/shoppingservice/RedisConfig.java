package com.ffreaky.shoppingservice;

import com.ffreaky.shoppingservice.food.service.RedisCacheable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCaching
@EnableRedisRepositories(basePackages = "com.ffreaky.shoppingservice.cache.repository")
public class RedisConfig {

    private final List<RedisCacheable> redisCacheableList;

    public RedisConfig(List<RedisCacheable> redisCacheableList) {
        this.redisCacheableList = redisCacheableList;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        final Map<String, RedisCacheConfiguration> cacheConfigurations = redisCacheableList.stream()
                .collect(HashMap::new, (map, cacheable) -> map.put(cacheable.cacheName(), cacheable.createCacheConfiguration()), HashMap::putAll);

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
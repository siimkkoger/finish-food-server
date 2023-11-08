package com.ffreaky.shoppingservice.food.service;

import org.springframework.data.redis.cache.RedisCacheConfiguration;

public interface RedisCacheable {
    String cacheName();
    RedisCacheConfiguration createCacheConfiguration();
}

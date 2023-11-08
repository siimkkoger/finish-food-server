package com.ffreaky.shoppingservice.food.service;

import com.ffreaky.shoppingservice.food.entity.QFoodEntity;
import com.ffreaky.shoppingservice.food.model.response.GetFoodResponse;
import com.ffreaky.shoppingservice.product.entity.QProductEntity;
import com.ffreaky.shoppingservice.product.entity.QProductProviderEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class FoodCacheService implements RedisCacheable {

    private static final String CACHE_KEY_TOTAL_FOODS = "FoodCacheService.findTotalFoods";
    private static final String CACHE_KEY_FOODS = "FoodsCacheService.foods";

    private final JPAQueryFactory queryFactory;

    public FoodCacheService(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Cacheable(value = CACHE_KEY_TOTAL_FOODS, key = "#cacheKey")
    public long findTotalFoods(BooleanExpression condition, String cacheKey) {
        var f = QFoodEntity.foodEntity;
        var p = QProductEntity.productEntity;
        var pp = QProductProviderEntity.productProviderEntity;
        var result = queryFactory
                .select(f.count())
                .from(f)
                .join(p).on(f.productId.eq(p.id))
                .join(pp).on(p.productProviderId.eq(pp.id))
                .where(condition)
                .fetchOne();

        return result == null ? 0 : result;
    }

    @Cacheable(value = CACHE_KEY_FOODS, key = "#cacheKey")
    public List<GetFoodResponse> findFoods(BooleanExpression condition, String cacheKey) {
        // TODO - Implement this method for default food page
        return null;
    }

    @CacheEvict(value = CACHE_KEY_TOTAL_FOODS, allEntries = true)
    public void evictCacheTotalFoods() {
        // This method is used to evict the cache
    }

    @Override
    public String cacheName() {
        return CACHE_KEY_TOTAL_FOODS;
    }

    @Override
    public RedisCacheConfiguration createCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}

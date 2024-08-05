package com.example.fintrack.api.common.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Default cache configuration
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();

        // Specific cache configurations
        var cacheConfigurations = Map.of(
                CacheName.CATEGORIES, categoryCacheConfiguration(defaultCacheConfig),
                CacheName.STATISTICS, statisticsCacheConfiguration(defaultCacheConfig)
        );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    private RedisCacheConfiguration categoryCacheConfiguration(RedisCacheConfiguration defaultCacheConfig) {
        return defaultCacheConfig.enableTimeToIdle().entryTtl(Duration.ofMinutes(5));
    }

    private RedisCacheConfiguration statisticsCacheConfiguration(RedisCacheConfiguration defaultCacheConfig) {
        return defaultCacheConfig.entryTtl(Duration.ofMinutes(2));
    }
}
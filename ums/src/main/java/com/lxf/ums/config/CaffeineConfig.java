package com.lxf.ums.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName : CaffeineConfig
 * @Description Cache本地缓存配置
 * @Date 2022/4/20 22:17
 * @Created lxf
 */
@Configuration
@EnableCaching
public class CaffeineConfig {

    @Bean("caffeineCache")
    public CacheManager cacheManager(){
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                //多少时间后无写入过期
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .initialCapacity(100);
        caffeineCacheManager.setCaffeine(caffeine);
        caffeineCacheManager.setAllowNullValues(true);
        return caffeineCacheManager;
    }

}

package com.orange.sqlexecutor.util.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/23
 * Time:下午4:24
 **/
@Configuration
public class SpringCacheConfig {

    /**
     * spring缓存配置，使用guava
     * @return
     */
    @Bean
    public GuavaCacheManager cacheManager(){
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(Integer.MAX_VALUE, TimeUnit.SECONDS));
        return cacheManager;
    }
}

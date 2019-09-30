package com.orange.sqlexecutor.util.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.stereotype.Component;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/23
 * Time:下午5:38
 **/
@Component
public class GuavaCacheUtil {

    private static GuavaCacheManager cacheManager;

    static final String CACHENAME = "ORANGE-GUAVA-CACHE";

    @Autowired
    public void setGuavaCacheManager(GuavaCacheManager cacheManager) {
        GuavaCacheUtil.cacheManager = cacheManager;
    }

    public static <T> T get(String key, Class<T> clazz) {
        Cache cache = cacheManager.getCache(CACHENAME);
        return cache.get(key, clazz);
    }

    public static void set(String key, Object value) {
        Cache cache = cacheManager.getCache(CACHENAME);
        cache.put(key, value);
    }

    public static void clear() {
        Cache cache = cacheManager.getCache(CACHENAME);
        cache.clear();
    }
}

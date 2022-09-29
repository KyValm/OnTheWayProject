package com.kenzie.appserver.config;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.Item;

import java.util.concurrent.TimeUnit;

public class CacheClient {
    private Cache<String, Item> cache;

    // Thanks for help from Justin's group on this cache Constructor
    public CacheClient(int expireTime, TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireTime, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public Item get(String key) {
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        cache.invalidate(key);
    }

    public void add(String key, Item value) {
        cache.put(key, value);
    }
}

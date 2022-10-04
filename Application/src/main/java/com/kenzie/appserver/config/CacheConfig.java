package com.kenzie.appserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    // Create a Cache here if needed
    @Bean
    public CacheClient myCache() {
        return new CacheClient(120, TimeUnit.SECONDS);
    }
}

package com.bookstore.config;

import com.bookstore.model.Book;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CacheConfig {

    // BUG: Memory leak - caches all search queries without limit
    private static final List<Book> searchCache = new java.util.concurrent.CopyOnWriteArrayList<>();

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        // BUG: No cache size limit - memory will grow indefinitely
        cacheManager.setCacheNames(Arrays.asList("books", "searches", "authors", "titles"));
        return cacheManager;
    }

    public static void addToSearchCache(Object result) {
        // BUG: Always adds to cache, never evicts - causes memory leak
        if (result instanceof List) {
            searchCache.addAll((List) result);
        }
    }
}

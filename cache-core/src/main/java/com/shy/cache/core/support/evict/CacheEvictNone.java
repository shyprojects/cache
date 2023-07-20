package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.model.CacheEntry;

/***
 * 无策略
 * @author shy
 * @date 2023-07-16 1:55
 */
public class CacheEvictNone<K, V> implements ICacheEvict<K,V> {
//    @Override
//    public CacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
//        return null;
//    }

    @Override
    public boolean evict(ICacheEvictContext<K, V> context) {
        return false;
    }
}

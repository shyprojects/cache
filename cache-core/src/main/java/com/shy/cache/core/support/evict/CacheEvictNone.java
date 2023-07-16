package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;

/***
 * 无策略
 * @author shy
 * @date 2023-07-16 1:55
 */
public class CacheEvictNone<K, V> implements ICacheEvict<K, V> {
    @Override
    public void evict(ICacheEvictContext<K, V> context) {
    }
}

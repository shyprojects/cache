package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvictContext;

/***
 * @author shy
 * @date 2023-07-24 19:50
 */
public class CacheEvictLruDoubleListMap<K, V> extends AbstractCacheEvict<K, V> {
    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }
}

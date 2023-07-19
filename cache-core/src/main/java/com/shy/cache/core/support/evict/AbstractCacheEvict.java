package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;

/***
 * @author shy
 * @date 2023-07-18 22:15
 */
public abstract class AbstractCacheEvict<K, V> implements ICacheEvict<K, V> {
    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEntry = doEvict(context);
        return cacheEntry;
    }

    protected abstract ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context);

    @Override
    public void update(K key) {

    }

    @Override
    public void remove(K key) {

    }
}

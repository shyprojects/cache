package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEvictContext;

/***
 * @author shy
 * @date 2023-07-15 23:41
 */
public class CacheEvictContext<K, V> implements ICacheEvictContext<K, V> {
    /**
     * 新加的key
     */
    private K key;

    /**
     * 最大的大小
     */
    private int size;

    /**
     * cache实现
     */
    private ICache<K, V> cache;

    @Override
    public K key() {
        return key;
    }


    @Override
    public ICache<K, V> cache() {
        return cache;
    }


    @Override
    public int size() {
        return size;
    }

    public CacheEvictContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public CacheEvictContext<K, V> size(int size) {
        this.size = size;
        return this;
    }

    public CacheEvictContext<K, V> cache(ICache<K, V> cache) {
        this.cache = cache;
        return this;
    }
}

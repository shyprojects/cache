package com.shy.cache.core.core;

import com.shy.cache.api.ICacheContext;
import com.shy.cache.api.ICacheEvict;

import java.util.Map;

/***
 * @author shy
 * @date 2023-07-14 23:18
 */
public class CacheContext<K, V> implements ICacheContext<K, V> {
    /**
     * map 信息
     */
    private Map<K, V> map;

    /**
     * 大小限制
     */
    private int size;

    /**
     * 驱除策略
     */
    private ICacheEvict<K,V> cacheEvict;

    @Override
    public Map<K, V> map() {
        return map;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public ICacheEvict<K, V> cacheEvict() {
        return cacheEvict;
    }

    public CacheContext<K, V> map(Map<K,V> map){
        this.map = map;
        return this;
    }

    public CacheContext<K,V> size(int size){
        this.size = size;
        return this;
    }

    public CacheContext<K,V> cacheEvict(ICacheEvict<K,V> cacheEvict){
        this.cacheEvict = cacheEvict;
        return this;
    }
}

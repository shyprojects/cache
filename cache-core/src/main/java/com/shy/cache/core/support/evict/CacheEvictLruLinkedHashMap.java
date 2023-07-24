package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;

import java.util.LinkedHashMap;
import java.util.Map;

/***
 * @author shy
 * @date 2023-07-25 0:48
 */
public class CacheEvictLruLinkedHashMap <K,V>extends LinkedHashMap<K,V>
        implements ICacheEvict<K,V> {

    /**
     * 是否移除标识
     */
    private volatile boolean removeFlag = false;

    /**
     * 最旧的一个元素
     */
    private transient Map.Entry<K, V> eldest = null;
    public CacheEvictLruLinkedHashMap() {
        super(16, 0.75f, true);
    }
    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        return null;
    }

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}

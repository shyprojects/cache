package com.shy.cache.core.support.persist;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICachePersist;

import java.util.concurrent.TimeUnit;

/***
 * 缓存持久化：适配器模式
 * @author shy
 * @date 2023-07-22 18:25
 */
public class CachePersistAdaptor<K,V> implements ICachePersist<K,V> {
    /**
     * 持久化方法
     * @param cache
     */
    @Override
    public void persist(ICache<K, V> cache) {
    }

    @Override
    public long delay() {
        return period();
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUtil() {
        return TimeUnit.SECONDS;
    }
}

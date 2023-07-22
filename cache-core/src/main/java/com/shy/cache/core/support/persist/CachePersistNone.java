package com.shy.cache.core.support.persist;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICachePersist;

import java.util.concurrent.TimeUnit;

/***
 * 不持久化策略
 * @author shy
 * @date 2023-07-21 15:23
 */
public class CachePersistNone<K,V> implements ICachePersist<K,V> {

    @Override
    public void persist(ICache<K, V> cache) {

    }

    @Override
    public long delay() {
        return 0;
    }

    @Override
    public long period() {
        return 0;
    }

    @Override
    public TimeUnit timeUtil() {
        return null;
    }
}

package com.shy.cache.core.support.persist;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICachePersist;

import java.util.concurrent.TimeUnit;

public class CachePersistAof<K,V> implements ICachePersist<K,V> {
    @Override
    public void persist(ICache<K, V> cache) {

    }

    @Override
    public long delay() {
        return 1;
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

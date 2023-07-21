package com.shy.cache.core.support.persist;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICachePersist;

/***
 * 不持久化策略
 * @author shy
 * @date 2023-07-21 15:23
 */
public class CachePersistNone<K,V> implements ICachePersist<K,V> {

    @Override
    public void persist(ICache<K, V> cache) {

    }
}

package com.shy.cache.api;
/***
 * 持久化接口
 * @author shy
 * @date 2023-07-21 15:23
 */
public interface ICachePersist<K,V> {
    void persist(final ICache<K,V> cache);
}

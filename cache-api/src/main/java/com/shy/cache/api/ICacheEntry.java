package com.shy.cache.api;

/***
 * 缓存实体
 * @author shy
 * @date 2023-07-18 21:59
 */
public interface ICacheEntry<K, V> {

    /**
     * @return key
     */
    K key();

    /**
     * @return value
     */
    V value();
}

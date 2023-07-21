package com.shy.cache.api;

/***
 * 缓存初始化接口
 * @author shy
 * @date 2023-07-21 10:28
 */
public interface ICacheLoad<K, V> {
    void load(final ICache<K, V> cache);
}


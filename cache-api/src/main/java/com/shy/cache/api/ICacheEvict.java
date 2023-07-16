package com.shy.cache.api;

/***
 * 驱逐策略
 * @author shy
 * @date 2023-07-12 23:44
 */
public interface ICacheEvict<K, V> {
    void evict(ICacheEvictContext<K, V> context);
}

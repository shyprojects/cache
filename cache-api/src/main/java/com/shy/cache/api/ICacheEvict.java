package com.shy.cache.api;

/***
 * 驱逐策略
 * @author shy
 * @date 2023-07-12 23:44
 */
public interface ICacheEvict<K, V> {
    /**
     * 驱逐方法
     *
     * @param context 驱逐的信息封装
     */
    ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context);

    /**
     * 更新 key 信息
     * @param key
     */
    void update(final K key);

    /**
     * 移除 key 信息
     * @param key
     */
    void remove(final K key);

}

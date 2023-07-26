package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICacheEvict;

/***
 * 便于获取缓存驱逐策略
 * @author shy
 * @date 2023-07-16 1:53
 */
public class CacheEvicts {


    private CacheEvicts(){
    }

    /**
     * 先进先出的驱逐策略
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> ICacheEvict<K, V> fifo() {
        return new CacheEvictFIFO<>();
    }

    /**
     * 无策略
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K,V> ICacheEvict<K,V> none(){
        return new CacheEvictNone<>();
    }

    /**
     * LRU算法
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> ICacheEvict<K, V> lru(){
        return new CacheEvictLru<>();
    }

    /**
     * lru的LinkedHashMap实现
     * @return
     */
    public static ICacheEvict<String, String> lruLinkedHashMap() {
        return new CacheEvictLruLinkedHashMap<>();
    }

    public static ICacheEvict<String, String> lruDoubleListMap() {
        return new CacheEvictLruDoubleListMap<>();
    }

    public static ICacheEvict<String, String> lru2Q() {
        return new CacheEvictLru2Q<>();
    }

    public static ICacheEvict<String, String> lru2() {
        return new CacheEvictLru2<>();
    }

    public static ICacheEvict<String, String> lfu() {
        return new CacheEvictLfu<>();
    }
}

package com.shy.cache.core.support.struct.lru;

import com.shy.cache.api.ICacheEntry;

/***
 * LRU map 接口
 * @author shy
 * @date 2023-07-25 18:19
 */
public interface ILruMap <K,V>{

    /**
     * 移除最老的元素
     * @return
     */
    ICacheEntry<K,V> removeEldest();

    /**
     * 更新key
     * @param key
     */
    void updateKey(K key);

    /**
     * 移除key
     * @param key
     */
    void removeKey(K key);

    /**
     * 判断是否为空
     * @return
     */
    boolean isEmpty();

    /**
     * 判断包含key
     * @return
     */
    boolean contains(K key);
}
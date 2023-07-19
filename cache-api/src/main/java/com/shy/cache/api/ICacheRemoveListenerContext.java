package com.shy.cache.api;

/***
 * @author shy
 * @date 2023-07-18 0:04
 */
public interface ICacheRemoveListenerContext<K, V> {
    /**
     * 清空的key
     * @return
     */
    K key();

    /**
     * 值
     * @return
     */
    V value();

    /**
     * 删除类型
     * @return
     */
    String type();

}

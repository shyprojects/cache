package com.shy.cache.api;

import java.util.concurrent.TimeUnit;

/***
 * 持久化接口
 * @author shy
 * @date 2023-07-21 15:23
 */
public interface ICachePersist<K,V> {
    void persist(final ICache<K,V> cache);

    /**
     * 延迟时间
     * @return
     */
    long delay();

    /**
     * 时间间隔
     * @return
     */
    long period();

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUtil();
}

package com.shy.cache.core.support.load;

import com.shy.cache.api.ICacheLoad;

/***
 * 加载策略工具类
 * @author shy
 * @date 2023-07-21 10:35
 */
public class CacheLoads<K, V> {
    private CacheLoads() {
    }

    /**
     * 无加载
     *
     * @return
     */
    public static <K, V> ICacheLoad<K, V> none() {
        return new CacheLoadNone<>();
    }
}

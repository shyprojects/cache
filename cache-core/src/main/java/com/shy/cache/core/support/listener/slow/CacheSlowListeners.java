package com.shy.cache.core.support.listener.slow;

import com.shy.cache.api.ICacheSlowListener;

import java.util.ArrayList;
import java.util.List;

/***
 * 慢日志监听器工具类
 * @author
 * @date 2023-07-22 13:41
 */
public class CacheSlowListeners {

    private CacheSlowListeners() {
    }


    /**
     * 默认实现
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> List<ICacheSlowListener<K, V>> defaults() {
        List<ICacheSlowListener<K, V>> listeners = new ArrayList<>();
        listeners.add(new CacheSlowListener<>());
        return listeners;
    }

    public static <K, V> List<ICacheSlowListener<K,V>> none() {
        return new ArrayList<>();
    }
}

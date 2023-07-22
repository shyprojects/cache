package com.shy.cache.core.support.listener.remove;

import com.shy.cache.api.ICacheRemoveListener;

import java.util.ArrayList;
import java.util.List;

/***
 * @author shy
 * @date 2023-07-20 0:22
 */
public class CacheRemoveListeners {

    public CacheRemoveListeners() {
    }

    /**
     * 默认监听器
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V>List<ICacheRemoveListener<K, V>> defaults(){
        List<ICacheRemoveListener<K, V>> listeners = new ArrayList<>();
        listeners.add(new CacheRemoveListener<>());
        return listeners;
    }

}

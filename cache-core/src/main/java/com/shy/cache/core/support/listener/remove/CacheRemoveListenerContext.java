package com.shy.cache.core.support.listener.remove;

import com.shy.cache.api.ICacheRemoveListenerContext;

/***
 * @author shy
 * @date 2023-07-20 0:22
 */
public class CacheRemoveListenerContext<K, V> implements ICacheRemoveListenerContext<K, V> {

    /**
     * key
     */
    private K key;

    /**
     * value
     */
    private V value;

    /**
     * type
     */
    private String type;

    public static <K, V> CacheRemoveListenerContext<K, V> newInstance() {
        return new CacheRemoveListenerContext<>();
    }


    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public String type() {
        return type;
    }

    public CacheRemoveListenerContext<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public CacheRemoveListenerContext<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public CacheRemoveListenerContext<K, V> type(String type) {
        this.type = type;
        return this;
    }
}

package com.shy.cache.core.core;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.core.exception.CacheRuntimeException;
import com.shy.cache.core.support.evict.CacheEvictContext;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/***
 * @author shy
 * @date 2023-07-14 23:27
 */
public class Cache<K,V> implements ICache<K ,V> {
    /**
     * map信息
     */
    private Map<K,V> map;

    /**
     * 限制大小
     */
    private int sizeLimit;

    /**
     * 驱逐策略
     */
    private ICacheEvict<K,V> cacheEvict;

    /**
     * 构造缓存
     * @param context
     */
    public Cache(CacheContext<K, V> context) {
        this.map = context.map();
        this.cacheEvict = context.cacheEvict();
        this.sizeLimit = context.size();
    }


    @Override
    public V put(K key, V value) {
        // 尝试驱逐
        CacheEvictContext<K, V> context = new CacheEvictContext<>();
        context.key(key).size(sizeLimit).cache(this);
        cacheEvict.evict(context);

        // 判断驱逐之后的信息
        if (isSizeLimit()){
            throw new CacheRuntimeException("当前队列已满，数据添加失败!");
        }
        // 执行添加
        return map.put(key,value);
    }

    /**
     * 判断是否达到最大限制
     * @return
     */
    private boolean isSizeLimit() {
        return this.size() >= sizeLimit;
    }

    @Override
    public ICache<K, V> expire(K key, long timeMills) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ICache<K, V> expireAt(K key, long timeMills) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

}

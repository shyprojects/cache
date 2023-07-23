package com.shy.cache.core.bs;


import com.github.houbb.heaven.util.common.ArgUtil;
import com.shy.cache.api.*;
import com.shy.cache.core.core.Cache;
import com.shy.cache.core.support.evict.CacheEvicts;
import com.shy.cache.core.support.listener.remove.CacheRemoveListeners;
import com.shy.cache.core.support.listener.slow.CacheSlowListeners;
import com.shy.cache.core.support.load.CacheLoads;
import com.shy.cache.core.support.persist.CachePersists;
import com.shy.cache.core.support.proxy.CacheProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 缓存引导类
 * @author shy
 * @date 2023-07-12 23:35
 */
public class CacheBs<K, V> {


    private CacheBs(){
    }

    /**
     * 创建对象实例
     * @return
     * @param <K> key
     * @param <V> value
     */
    public static <K, V> CacheBs<K, V> newInstance() {
        return new CacheBs<>();
    }

    /**
     * map实现
     */
    private Map<K,V> map = new HashMap<>();

    /**
     * 大小限制
     */
    private int size = Integer.MAX_VALUE;

    /**
     * 驱逐策略
     */
    private ICacheEvict<K, V> evict = CacheEvicts.fifo();
    /**s
     * 删除监听器
     */
    private List<ICacheRemoveListener<K,V>> removeListeners = CacheRemoveListeners.defaults();
    /**
     * 缓存初始化策略
     */
    private ICacheLoad<K,V> load = CacheLoads.none();
    /**
     * 缓存持久化策略
     */
    private ICachePersist<K,V> persist = CachePersists.none();

    /**
     * 慢日志监听器
     */
    private List<ICacheSlowListener<K,V>> slowListeners = CacheSlowListeners.none();



    /**
     * 设置大小限制
     * @param size
     * @return
     */
    public CacheBs<K ,V> size(int size){
        this.size = size;
        return this;
    }

    /**
     * map实现
     * @param map
     * @return
     */
    public CacheBs<K,V> map(Map<K,V> map){
        ArgUtil.notNull(map,"map");
        this.map = map;
        return this;
    }

    /**
     * 设置驱逐策略
     * @param evict
     * @return
     */
    public CacheBs<K ,V> evict(ICacheEvict<K,V> evict){
        this.evict = evict;
        return this;
    }

    public CacheBs<K,V> load(ICacheLoad<K,V> load){
        this.load = load;
        return this;
    }

    public CacheBs<K ,V> addRemoveListener(ICacheRemoveListener<K,V> listener){
        ArgUtil.notNull(listener,"removeListener");
        this.removeListeners.add(listener);
        return this;
    }

    public CacheBs<K,V> addSlowListener(ICacheSlowListener<K,V> listener){
        ArgUtil.notNull(listener,"slowListener");
        this.slowListeners.add(listener);
        return this;
    }


    public CacheBs<K,V> persist(ICachePersist<K,V> persist){
        this.persist = persist;
        return this;
    }

    /**
     * 构建缓存信息
     * @return
     */
    public ICache<K ,V> build(){
        Cache<K, V> cache = new Cache<>();
        cache.sizeLimit(size);
        cache.cacheEvict(evict);
        cache.map(map);
        cache.removeListeners(removeListeners);
        cache.load(load);
        cache.persist(persist);
        cache.slowListeners(slowListeners);

        //调用初始化方法
        cache.init();
        return CacheProxy.getProxy(cache);
    }



}


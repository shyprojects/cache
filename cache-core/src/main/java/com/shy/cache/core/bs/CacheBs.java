package com.shy.cache.core.bs;


import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheRemoveListener;
import com.shy.cache.core.core.Cache;
import com.shy.cache.core.core.CacheContext;
import com.shy.cache.core.support.evict.CacheEvicts;
import com.shy.cache.core.support.listener.CacheRemoveListeners;
import com.shy.cache.core.support.proxy.CacheProxy;
import com.shy.cache.core.util.ArgUtil;

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

    public CacheBs<K ,V> addRemoveListener(ICacheRemoveListener<K,V> listener){
        this.removeListeners.add(listener);
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
        return CacheProxy.getProxy(cache);
    }



}


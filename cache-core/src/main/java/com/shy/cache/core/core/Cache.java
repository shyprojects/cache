package com.shy.cache.core.core;

import com.shy.cache.annotation.CacheInterceptor;
import com.shy.cache.api.*;
import com.shy.cache.core.constant.enums.CacheRemoveType;
import com.shy.cache.core.exception.CacheRuntimeException;
import com.shy.cache.core.support.evict.CacheEvictContext;
import com.shy.cache.core.support.expire.CacheExpire;
import com.shy.cache.core.support.listener.remove.CacheRemoveListenerContext;
import com.shy.cache.core.support.persist.InnerCachePersist;

import java.util.*;

/***
 * @author shy
 * @date 2023-07-14 23:27
 */
public class Cache<K, V> implements ICache<K, V> {
    /**
     * map信息
     */
    private Map<K, V> map;

    /**
     * 限制大小
     */
    private int sizeLimit;

    /**
     * 驱逐策略
     */
    private ICacheEvict<K, V> cacheEvict;

    /**
     * 过期策略
     * 暂时不暴露，过期策略写死
     */
    private ICacheExpire<K, V> cacheExpire;

    private List<ICacheSlowListener<K,V>> slowListeners;

    /**
     * 删除监听类
     */
    private List<ICacheRemoveListener<K, V>> removeListeners;

    /**
     * 缓存加载策略
     */
    private ICacheLoad<K,V> load;

    /**
     * 持久化策略
     */
    private ICachePersist<K,V> persist;

//    /**
//     * 构造缓存
//     * @param context
//     */
//    public Cache(CacheContext<K, V> context) {
//        this.map = context.map();
//        this.cacheEvict = context.cacheEvict();
//        this.sizeLimit = context.size();
//        this.cacheExpire = new CacheExpire<>(this);
//    }

    @Override
    public ICacheLoad<K,V> load(){
        return this.load;
    }

    public ICache<K,V> load(ICacheLoad<K,V> load){
        this.load = load;
        return this;
    }

    /**
     * 初始化方法
     */
    public void init(){
        this.load.load(this);
        this.cacheExpire = new CacheExpire<>(this);

        //持久化设置
        if (this.persist != null){
            new InnerCachePersist<>(this,this.persist);
        }
    }


    @Override
    @CacheInterceptor
    public ICacheExpire<K,V> expire(){
        return this.cacheExpire;
    }

    /**
     * 设置map信息
     *
     * @param map
     * @return
     */
    public ICache<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    /**
     * 设置sizeLimit
     *
     * @param sizeLimit
     * @return
     */
    public ICache<K, V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    /**
     *
     * @param persist
     * @return
     */
    public ICache<K,V> persist(ICachePersist<K,V> persist){
        this.persist = persist;
        return this;
    }

    /**
     * 设置cacheEvict
     *
     * @param cacheEvict
     * @return
     */
    public ICache<K, V> cacheEvict(ICacheEvict<K, V> cacheEvict) {
        this.cacheEvict = cacheEvict;
        return this;
    }

    @Override
    @CacheInterceptor
    public V put(K key, V value) {
        // 尝试驱逐
        CacheEvictContext<K, V> context = new CacheEvictContext<>();
        context.key(key).size(sizeLimit).cache(this);
        boolean evictResult = cacheEvict.evict(context);
        if (evictResult) {
            ICacheRemoveListenerContext<K, V> removeListenerContext = CacheRemoveListenerContext.<K, V>newInstance().key(key).value(value).type(CacheRemoveType.EVICT.code());
            for (ICacheRemoveListener<K, V> removeListener : this.removeListeners) {
                removeListener.listen(removeListenerContext);
            }
        }

        // 判断驱逐之后的信息
        if (isSizeLimit()) {
            throw new CacheRuntimeException("当前队列已满，数据添加失败!");
        }
        // 执行添加
        return map.put(key, value);
    }

    /**
     * 判断是否达到最大限制
     *
     * @return
     */
    private boolean isSizeLimit() {
        return this.size() >= sizeLimit;
    }

    @Override
    public ICache<K, V> expire(K key, long timeMills) {
        long expireTime = System.currentTimeMillis() + timeMills;
        return this.expireAt(key, expireTime);
    }

    @Override
    public ICache<K, V> expireAt(K key, long timeMills) {
        this.cacheExpire.expire(key, timeMills);
        return this;
    }

    @Override
    public ICacheExpire<K, V> cacheExpire() {
        return this.cacheExpire;
    }

    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {
        return removeListeners;
    }

    @Override
    public List<ICacheSlowListener<K, V>> slowListeners() {
        return slowListeners;
    }

    public ICache<K, V> removeListeners(List<ICacheRemoveListener<K, V>> removeListeners) {
        this.removeListeners = removeListeners;
        return this;
    }

    public ICache<K,V> slowListeners(List<ICacheSlowListener<K,V>> slowListeners){
        this.slowListeners = slowListeners;
        return this;
    }

    @Override
    @CacheInterceptor(refresh = true)
    public int size() {
        return map.size();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @CacheInterceptor
    public V get(Object key) {
        //刷新过期信息,因为get(key)只需要获取这个key是否存在，因此只需要刷新这一个key即可
        K genericKey = (K) key;
        this.cacheExpire.refreshExpire(Collections.singletonList(genericKey));
        return map.get(key);
    }

    @Override
    @CacheInterceptor
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    @CacheInterceptor
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    @CacheInterceptor(refresh = true)
    public void clear() {
        map.clear();
    }

    /**
     * 该方法调用结果返回之前需要刷新所有的key
     *
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * 该方法调用结果返回之前需要刷新所有的key
     *
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public Collection<V> values() {
        return map.values();
    }

    /**
     * 该方法调用结果返回之前需要刷新所有的key
     *
     * @return
     */
    @Override
    @CacheInterceptor(refresh = true)
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    /**
     * 该方法调用结果返回之前需要刷新所有的key
     *
     * @return
     */
    @Deprecated
    private void refreshExpireAllKeys() {
        this.cacheExpire.refreshExpire(map.keySet());
    }
}

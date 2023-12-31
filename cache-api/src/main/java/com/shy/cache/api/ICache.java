package com.shy.cache.api;

import java.util.List;
import java.util.Map;

/***
 * @author shy
 * @date 2023-07-14 22:59
 */
public interface ICache<K, V> extends Map<K, V> {

    /**
     * 类似于redis的定期删除 + 惰性删除
     *
     * 指定过期时间
     * @param key
     * @param timeMills 毫秒
     * @return
     */
    ICache<K,V> expire(final K key,final long timeMills);

    /**
     * 获取缓存过期处理类
     * @return
     */
    ICacheExpire<K,V> expire();

    /**
     * 再指定过期时间
     * @param key
     * @param timeMills
     * @return
     */
    ICache<K,V> expireAt(final K key,final long timeMills);

    /**
     * 获取缓存的过期处理类
     * @return 处理类的实现
     */
    ICacheExpire<K,V> cacheExpire();

    /**
     * 删除监听器列表
     * @return 监听器列表
     */
    List<ICacheRemoveListener<K,V>> removeListeners();

    /**
     * 慢监听器列表
     * @return
     */
    List<ICacheSlowListener<K,V>> slowListeners();

    /**
     * @return 加载策略
     */
    ICacheLoad<K,V> load();

    /**
     * @return 持久化策略
     */
    ICachePersist<K,V> persist();

    /**
     * 淘汰策略
     * @return
     */
    ICacheEvict<K,V> evict();

}

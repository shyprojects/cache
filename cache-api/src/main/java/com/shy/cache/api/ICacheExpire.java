package com.shy.cache.api;

import java.util.Collection;

/***
 * 缓存过期的接口
 * @author shy
 * @date 2023-07-16 15:29
 */
public interface ICacheExpire<K, V> {

    /**
     * 指定过期信息
     * @param key 过期的key
     * @param expireAt 什么时候过期
     */
    void expire(K key, long expireAt);

    /**
     * 惰性删除需要处理的key
     * @param keyList
     */
    void refreshExpire(Collection<K> keyList);

    /**
     * 获取key的过期时间，如果不存在，则返回null
     * @param key
     * @return
     */
    Long expireTime(K key);

}

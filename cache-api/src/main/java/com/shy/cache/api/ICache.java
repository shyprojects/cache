package com.shy.cache.api;

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
     * 再指定过期时间
     * @param key
     * @param timeMills
     * @return
     */
    ICache<K,V> expireAt(final K key,final long timeMills);

}

package com.shy.cache.core.support.load;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheLoad;
/***
 * 加载策略-无
 * @author shy
 * @date 2023-07-21 10:30
 */
public class CacheLoadNone<K,V> implements ICacheLoad<K,V> {
    @Override
    public void load(ICache<K, V> cache) {
    }
}

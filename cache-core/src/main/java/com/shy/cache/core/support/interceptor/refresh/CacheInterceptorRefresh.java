package com.shy.cache.core.support.interceptor.refresh;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheInterceptor;
import com.shy.cache.api.ICacheInterceptorContext;
import org.apache.log4j.Logger;

/***
 * @author shy
 * @date 2023-07-19 23:12
 */
public class CacheInterceptorRefresh<K, V> implements ICacheInterceptor<K, V> {

    private final Logger log = Logger.getLogger(CacheInterceptorRefresh.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Refresh start");
        final ICache<K, V> cache = context.cache();
        cache.cacheExpire().refreshExpire(cache.keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {

    }
}

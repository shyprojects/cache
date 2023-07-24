package com.shy.cache.core.support.interceptor.evict;

import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheInterceptor;
import com.shy.cache.api.ICacheInterceptorContext;

import java.lang.reflect.Method;

/***
 * 驱逐策略拦截器
 * @author shy
 * @date 2023-07-19 21:38
 */
public class CacheInterceptorEvict<K,V> implements ICacheInterceptor<K,V> {

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        ICacheEvict<K, V> evict = context.cache().evict();

        Method method = context.method();
        K key = (K)context.params()[0];
        if ("remove".equals(method.getName())){
            evict.remove(key);
        }else{
            evict.update(key);
        }
    }
}

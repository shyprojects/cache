package com.shy.cache.core.support.interceptor;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheInterceptorContext;

import java.lang.reflect.Method;

/***
 * @author shy
 * @date 2023-07-19 22:51
 */
public class CacheInterceptorContext<K, V> implements ICacheInterceptorContext<K, V> {

    /**
     * 缓存
     */
    private ICache<K,V> cache;

    /**
     * 执行的方法
     */
    private Method method;

    /**
     * 参数信息
     */
    private Object[] params;

    /**
     * 结果信息
     */
    private Object result;

    /**
     * 开始时间
     */
    private long startMillis;

    /**
     * 结束时间
     */
    private long endMillis;

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public Object[] params() {
        return params;
    }

    @Override
    public Object result() {
        return result;
    }

    @Override
    public long startMillis() {
        return startMillis;
    }

    @Override
    public long endMillis() {
        return endMillis;
    }
}

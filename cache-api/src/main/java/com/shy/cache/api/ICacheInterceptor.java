package com.shy.cache.api;

/***
 * 拦截器接口
 * @author shy
 * @date 2023-07-19 22:47
 */
public interface ICacheInterceptor<K,V> {

    /**
     * 方法执行之前
     * @param context
     */
    void before(ICacheInterceptorContext<K,V> context);

    /**
     * 方法执行之后
     * @param context
     */
    void after(ICacheInterceptorContext<K,V> context);
}

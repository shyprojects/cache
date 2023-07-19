package com.shy.cache.api;

import java.lang.reflect.Method;

/***
 * 拦截器上下文
 * @author shy
 * @date 2023-07-19 22:44
 */
public interface ICacheInterceptorContext<K,V> {
    /**
     * 缓存信息
     * @return
     */
    ICache<K,V> cache();

    /**
     * 方法信息
     * @return
     */
    Method method();

    /**
     * 参数信息
     * @return
     */
    Object[] params();

    /**
     * 返回值信息
     * @return
     */
    Object result();

    /**
     * 开始时间
     * @return
     */
    long startMillis();

    /**
     * 结束时间
     * @return
     */
    long endMillis();
}

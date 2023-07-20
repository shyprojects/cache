package com.shy.cache.core.support.proxy.bs;

import com.shy.cache.annotation.CacheInterceptor;
import com.shy.cache.api.ICache;

import java.lang.reflect.Method;

/***
 * 拦截器代理上下文
 * @author shy
 * @date 2023-07-20 10:14
 */
public interface ICacheProxyBsContext {
    /**
     * 拦截器信息
     * @return
     */
    CacheInterceptor interceptor();

    /**
     * 获取代理对象信息
     * @return
     */
    ICache target();

    /**
     * 参数信息
     * @return
     */
    Object[] params();

    /**
     * 目标对象
     * @param target
     * @return
     */
    ICacheProxyBsContext target(final ICache target);

    /**
     * 方法信息
     * @return
     */
    Method method();

    /**
     * 方法执行
     * @return
     * @throws Throwable 抛出的异常
     */
    Object process() throws Throwable;
}

package com.shy.cache.core.support.proxy.bs;

import com.shy.cache.annotation.CacheInterceptor;
import com.shy.cache.api.ICache;

import java.lang.reflect.Method;
/***
 * 代理引导类上下文
 * @author shy
 * @date 2023-07-20 10:14
 */
public class CacheProxyBsContext implements ICacheProxyBsContext{

    /**
     * 目标对象
     */
    private ICache target;

    /**
     * 参数
     */
    private Object[] params;

    /**
     * 拦截器
     */
    private CacheInterceptor interceptor;

    /**
     * 方法
     */
    private Method method;

    public static CacheProxyBsContext newInstance(){
        return new CacheProxyBsContext();
    }

    @Override
    public CacheInterceptor interceptor() {
        return this.interceptor;
    }

    @Override
    public ICache target() {
        return this.target;
    }

    @Override
    public Object[] params() {
        return this.params;
    }

    @Override
    public ICacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    @Override
    public Method method() {
        return this.method;
    }

    @Override
    public Object process() throws Throwable {
        return this.method.invoke(target,params);
    }

    public CacheProxyBsContext method(Method method){
        this.method = method;
        this.interceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }
    public CacheProxyBsContext params(Object[] params){
        this.params = params;
        return this;
    }
}

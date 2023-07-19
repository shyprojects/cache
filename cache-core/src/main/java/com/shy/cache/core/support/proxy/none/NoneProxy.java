package com.shy.cache.core.support.proxy.none;

import com.shy.cache.core.support.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/***
 * @author shy
 * @date 2023-07-19 22:18
 */
public class NoneProxy implements InvocationHandler, ICacheProxy {

    /**
     * 代理对象
     */
    private final Object target;

    public NoneProxy(Object target){
        this.target = target;
    }

    /**
     * 返回原始对象，没有代理
     * @return
     */
    @Override
    public Object proxy() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(args);
    }
}

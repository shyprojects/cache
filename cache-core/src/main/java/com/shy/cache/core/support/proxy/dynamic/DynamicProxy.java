package com.shy.cache.core.support.proxy.dynamic;

import com.shy.cache.api.ICache;
import com.shy.cache.core.support.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/***
 * @author shy
 * @date 2023-07-17 22:09
 */
public class DynamicProxy implements InvocationHandler, ICacheProxy {

    /**
     * 被代理的对象
     */
    private final ICache target;

    public DynamicProxy(ICache target){
        this.target = target;
    }

    @Override
    public Object proxy() {
        InvocationHandler handler = new DynamicProxy(target);
        return Proxy.newProxyInstance(target.getClass().getClassLoader()
                , target.getClass().getInterfaces(),handler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }
}

package com.shy.cache.core.support.proxy.cglib;

import com.shy.cache.api.ICache;
import com.shy.cache.core.support.proxy.ICacheProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/***
 * @author shy
 * @date 2023-07-18 21:40
 */
public class CglibProxy implements MethodInterceptor, ICacheProxy {

    private final ICache target;

    public CglibProxy(ICache target){
        this.target = target;
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        // 目标类对象

        return null;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return null;
    }
}

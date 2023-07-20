package com.shy.cache.core.support.proxy.cglib;

import com.shy.cache.api.ICache;
import com.shy.cache.core.support.proxy.ICacheProxy;
import com.shy.cache.core.support.proxy.bs.CacheProxyBs;
import com.shy.cache.core.support.proxy.bs.CacheProxyBsContext;
import com.shy.cache.core.support.proxy.bs.ICacheProxyBsContext;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/***
 * @author shy
 * @date 2023-07-18 21:40
 */
public class CglibProxy implements MethodInterceptor, ICacheProxy {

    /**
     * 被代理的对象
     */
    private final ICache target;

    public CglibProxy(ICache target){
        this.target = target;
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        // 目标类对象
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] params, MethodProxy proxy) throws Throwable {
        ICacheProxyBsContext context = CacheProxyBsContext.newInstance().method(method).params(params).target(target);
        return CacheProxyBs.newInstance().context(context).execute();
    }
}

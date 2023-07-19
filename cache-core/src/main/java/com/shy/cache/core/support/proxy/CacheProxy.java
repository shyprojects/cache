package com.shy.cache.core.support.proxy;

import com.shy.cache.api.ICache;
import com.shy.cache.core.support.proxy.cglib.CglibProxy;
import com.shy.cache.core.support.proxy.dynamic.DynamicProxy;
import com.shy.cache.core.support.proxy.none.NoneProxy;
import com.shy.cache.core.util.ObjectUtil;

import java.lang.reflect.Proxy;

/***
 * @author shy
 * @date 2023-07-19 22:24
 */
public final class CacheProxy {
    private CacheProxy(){}

    public static <K,V> ICache<K,V> getProxy(ICache<K,V> cache){
        if (ObjectUtil.isNull(cache)) {
            return (ICache<K, V>) new NoneProxy(cache).proxy();
        }
        final Class clazz = cache.getClass();
        if (clazz.isInstance(clazz) || Proxy.isProxyClass(clazz)){
            return (ICache<K,V>) new DynamicProxy(cache).proxy();
        }
        return (ICache<K,V>) new CglibProxy(cache).proxy();
    }
}


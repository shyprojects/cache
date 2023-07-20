package com.shy.cache.core.support.interceptor;


import com.shy.cache.api.ICacheInterceptor;
import com.shy.cache.core.support.interceptor.common.CacheInterceptorTimeCost;
import com.shy.cache.core.support.interceptor.refresh.CacheInterceptorRefresh;

import java.util.ArrayList;
import java.util.List;

/***
 * 缓存拦截器工具类
 * @author shy
 * @date 2023-07-19 22:42
 */
public final class CacheInterceptors {
    /**
     * 默认通用拦截器
     * @return
     */
    public static List<ICacheInterceptor> defaultCommonList(){
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorTimeCost());
        return list;
    }

    /**
     * 默认刷新拦截器
     * @return
     */
    public static List<ICacheInterceptor> defaultRefrshList(){
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorRefresh());
        return list;
    }
}

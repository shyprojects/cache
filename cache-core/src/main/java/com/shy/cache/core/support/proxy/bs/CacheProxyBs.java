package com.shy.cache.core.support.proxy.bs;

import com.shy.cache.annotation.CacheInterceptor;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheInterceptor;
import com.shy.cache.api.ICachePersist;
import com.shy.cache.core.support.interceptor.CacheInterceptorContext;
import com.shy.cache.core.support.interceptor.CacheInterceptors;
import com.shy.cache.core.support.persist.CachePersistAof;

import java.util.List;

/***
 * 代理方法调用引导类
 * @author shy
 * @date 2023-07-20 10:14
 */
public final class CacheProxyBs {
    private CacheProxyBs(){
    }

    /**
     * 代理上下文
     */
    private ICacheProxyBsContext context;

    /**
     * 默认通用拦截器
     */
    private final List<ICacheInterceptor> commonInterceptors = CacheInterceptors.defaultCommonList();

    /**
     * 通用刷新拦截器
     */
    private final List<ICacheInterceptor> refreshInterceptors = CacheInterceptors.defaultRefreshList();

    /**
     * aof持久化拦截器
     */
    private final ICacheInterceptor aofInterceptor = CacheInterceptors.aof();

    /**
     *
     */
    private final ICacheInterceptor evictInterceptor = CacheInterceptors.evict();

    /**
     * 新建实例对象
     * @return
     */
    public static CacheProxyBs newInstance(){
        return new CacheProxyBs();
    }

    public CacheProxyBs context(ICacheProxyBsContext context){
        this.context = context;
        return  this;
    }

    public Object execute() throws Throwable{
        final long startMillis = System.currentTimeMillis();
        final ICache cache = context.target();
        CacheInterceptorContext cacheInterceptorContext = CacheInterceptorContext.newInstance()
                .startMillis(startMillis)
                .params(context.params())
                .cache(context.target())
                .method(context.method());
        CacheInterceptor interceptor = context.interceptor();

        //方法执行之前
        this.interceptorHandler(interceptor,cacheInterceptorContext,cache,true);
        //调用方法
        Object result = context.process();
        final long endMillis = System.currentTimeMillis();
        cacheInterceptorContext.endMillis(endMillis);
        //方法执行之后
        this.interceptorHandler(interceptor,cacheInterceptorContext,cache,false);
        return result;
    }

    public void interceptorHandler(CacheInterceptor cacheInterceptor
            ,CacheInterceptorContext interceptorContext,ICache cache,boolean before){
        if (cacheInterceptor != null){
            // 通用拦截器
            if (cacheInterceptor.common()){
                for (ICacheInterceptor commonInterceptor : commonInterceptors) {
                    if (before){
                        commonInterceptor.before(interceptorContext);
                    }else{
                        commonInterceptor.after(interceptorContext);
                    }
                }
            }
            // 刷新拦截器
            if (cacheInterceptor.refresh()){
                for (ICacheInterceptor refreshInterceptor : refreshInterceptors) {
                    if (before){
                        refreshInterceptor.before(interceptorContext);
                    }else{
                        refreshInterceptor.after(interceptorContext);
                    }
                }
            }
            // aof持久化拦截器
            ICachePersist persist = cache.persist();
            if (cacheInterceptor.aof() && persist instanceof CachePersistAof){
                if (before){
                    aofInterceptor.before(interceptorContext);
                }else {
                    aofInterceptor.after(interceptorContext);
                }
            }
            // 驱逐策略
            if(cacheInterceptor.evict()){
                if (before){
                    evictInterceptor.before(interceptorContext);
                }else{
                    evictInterceptor.after(interceptorContext);
                }
            }
        }
    }
}


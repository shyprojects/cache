package com.shy.cache.core.support.interceptor.common;

import com.shy.cache.api.ICacheInterceptor;
import com.shy.cache.api.ICacheInterceptorContext;
import org.apache.log4j.Logger;

/***
 * 耗时统计:
 * 1.耗时
 * 2.慢日志
 * @author shy
 * @date 2023-07-19 23:07
 */
public class CacheInterceptorTimeCost<K, V> implements ICacheInterceptor<K, V> {
    private final Logger log = Logger.getLogger(CacheInterceptorTimeCost.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.info("Cost start, method:" + context.method().getName());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        long costTime = context.endMillis() - context.startMillis();
        log.info("Cost end,method:" + context.method().getName() + "cost:" + costTime + "ms");
    }
}

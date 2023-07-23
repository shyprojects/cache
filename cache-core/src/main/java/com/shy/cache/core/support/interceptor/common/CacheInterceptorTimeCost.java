package com.shy.cache.core.support.interceptor.common;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.shy.cache.api.ICacheInterceptor;
import com.shy.cache.api.ICacheInterceptorContext;
import com.shy.cache.api.ICacheSlowListener;
import com.shy.cache.api.ICacheSlowListenerContext;
import com.shy.cache.core.support.listener.slow.CacheSlowListenerContext;
import org.apache.log4j.Logger;

import java.util.List;

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
        log.info("Cost end,method:" + context.method().getName() + ",cost:" + costTime + "ms");
        List<ICacheSlowListener<K, V>> slowListeners = context.cache().slowListeners();
        // 遍历slowListener
        if (CollectionUtil.isNotEmpty(slowListeners)) {
            //准备参数
            ICacheSlowListenerContext slowListenerContext = CacheSlowListenerContext.newInstance().startMillis(context.startMillis())
                    .costMillis(costTime)
                    .endMillis(context.endMillis())
                    .methodName(context.method().getName())
                    .params(context.params())
                    .result(context.result());

            for (ICacheSlowListener<K, V> slowListener : slowListeners) {
                long slowerThanMills = slowListener.slowerThanMills();
                //对于慢操作记录日志
                if (costTime >= slowerThanMills) {
                    slowListener.listen(slowListenerContext);
                }
            }
        }
    }
}

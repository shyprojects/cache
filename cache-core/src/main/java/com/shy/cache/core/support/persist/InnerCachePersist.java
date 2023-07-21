package com.shy.cache.core.support.persist;


import com.shy.cache.api.ICache;
import com.shy.cache.api.ICachePersist;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/***
 * 内部缓存持久化类
 * @author shy
 * @date 2023-07-21 17:43
 */
public class InnerCachePersist<K,V> {
    private final Logger log = Logger.getLogger(InnerCachePersist.class);

    /**
     * 缓存信息
     */
    private final ICache<K,V> cache;
    /**
     * 持久化信息
     */
    private final ICachePersist<K,V> persist;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public InnerCachePersist(ICache<K,V> cache,ICachePersist<K,V> persist){
        this.cache = cache;
        this.persist = persist;

        this.init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(()->{
            try {
                log.info("开始持久化...");
                persist.persist(cache);
                log.info("结束持久化...");
            } catch (Exception e) {
                log.info("持久化异常" + e);
            }
        },0,10, TimeUnit.SECONDS);
    }
}

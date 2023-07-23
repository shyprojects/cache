package com.shy.cache.core.support.interceptor.aof;

import com.alibaba.fastjson.JSON;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheInterceptor;
import com.shy.cache.api.ICacheInterceptorContext;
import com.shy.cache.api.ICachePersist;
import com.shy.cache.core.model.PersistAofEntity;
import com.shy.cache.core.support.persist.CachePersistAof;
import org.apache.log4j.Logger;

/***
 * @author shy
 * @date 2023-07-23 1:21
 */
public class CacheInterceptorAof<K,V> implements ICacheInterceptor<K,V> {

    private static final Logger log = Logger.getLogger(CacheInterceptorAof.class);
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        CachePersistAof<K,V> persist = (CachePersistAof<K,V>)cache.persist();
        if (persist instanceof CachePersistAof){
            String methodName = context.method().getName();
            Object[] params = context.params();
            PersistAofEntity aofEntity = PersistAofEntity.newInstance();
            aofEntity.setMethodName(methodName);
            aofEntity.setParams(params);

            String jsonString = JSON.toJSONString(aofEntity);
            log.info("AOF开始追加文件" + jsonString);
            persist.append(jsonString);
            log.info("AOF结束追加文件" + jsonString);
        }
    }
}

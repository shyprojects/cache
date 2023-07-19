package com.shy.cache.core.support.listener;

import com.shy.cache.api.ICacheRemoveListener;
import com.shy.cache.api.ICacheRemoveListenerContext;
import org.apache.log4j.Logger;

/***
 * @author shy
 * @date 2023-07-18 0:11
 */
public class CacheRemoveListener<K, V> implements ICacheRemoveListener<K, V> {

    private static final Logger log = Logger.getLogger(CacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.info("Remove key:" + context.key() + ",value:" + context.value() + ",type" + context.type());
    }
}

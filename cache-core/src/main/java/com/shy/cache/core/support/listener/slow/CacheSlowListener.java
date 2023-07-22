package com.shy.cache.core.support.listener.slow;

import com.alibaba.fastjson.JSON;
import com.shy.cache.api.ICacheSlowListener;
import com.shy.cache.api.ICacheSlowListenerContext;
import org.apache.log4j.Logger;

/***
 * 慢日志监听器
 * @author
 * @date 2023-07-22 13:41
 */
public class CacheSlowListener<K, V> implements ICacheSlowListener<K, V> {

    private final Logger log = Logger.getLogger(CacheSlowListener.class);

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn(
                "[slow] methodName:"
                + context.methodName()
                + ",params:" + JSON.toJSONString(context.params())
                + ",cost time:" + context.costTime()
        );
    }

    @Override
    public long slowerThanMills() {
        return 1000l;
    }
}

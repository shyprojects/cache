package com.shy.cache.api;
/***
 * 慢操作监听器通用接口
 * @author shy
 * @date 2023-07-22 12:04
 */
public interface ICacheSlowListener<K, V> {
    /**
     * 监听方法
     * @param context
     */
    void listen(final ICacheSlowListenerContext context);

    /**
     * 慢日志的预制
     * @return
     */
    long slowerThanMills();
}

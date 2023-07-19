package com.shy.cache.api;

/***
 * 监听器接口
 * 1.耗时统计
 * 2.监听器
 * @author shy
 * @date 2023-07-18 0:02
 */
public interface ICacheRemoveListener <K ,V>{
    /**
     * 监听
     */
    void listen(ICacheRemoveListenerContext<K,V> context);
}

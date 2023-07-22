package com.shy.cache.api;
/***
 * 慢操作监听器上下文
 * @author shy
 * @date 2023-07-22 12:04
 */
public interface ICacheSlowListenerContext {

    /**
     * 方法名
     * @return
     */
    String methodName();

    /**
     * 参数列表
     * @return
     */
    Object[] params();

    /**
     * 开始时间
     * @return
     */
    long startMillis();

    /**
     * 结束时间
     * @return
     */
    long endMillis();

    /**
     * 花费时间
     * @return
     */
    long costTime();

    /**
     * 返回结果
     * @return
     */
    Object result();
}

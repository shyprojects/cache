package com.shy.cache.core.support.listener.slow;

import com.shy.cache.api.ICacheSlowListenerContext;

/***
 * 慢日志监听器上下文
 * @author
 * @date 2023-07-22 13:41
 */
public class CacheSlowListenerContext implements ICacheSlowListenerContext {

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数列表
     */
    private Object[] params;

    /**
     * 返回值
     */
    private Object result;


    /**
     * 开始时间
     */
    private long startTimeMillis;

    /**
     * 结束时间
     */
    private long endTimeMillis;

    /**
     * 花费时间
     */
    private long costTimeMillis;

    public static CacheSlowListenerContext newInstance() {
        return new CacheSlowListenerContext();
    }

    @Override
    public String methodName() {
        return methodName;
    }

    @Override
    public Object[] params() {
        return params;
    }

    @Override
    public long startMillis() {
        return startTimeMillis;
    }

    @Override
    public long endMillis() {
        return endTimeMillis;
    }

    @Override
    public long costTime() {
        return costTimeMillis;
    }

    @Override
    public Object result() {
        return result;
    }

    public CacheSlowListenerContext methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public CacheSlowListenerContext params(Object[] params) {
        this.params = params;
        return this;
    }

    public CacheSlowListenerContext startMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
        return this;
    }

    public CacheSlowListenerContext endMillis(long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
        return this;
    }

    public CacheSlowListenerContext costMillis(long costTimeMillis) {
        this.costTimeMillis = costTimeMillis;
        return this;
    }

    public CacheSlowListenerContext result(Object result) {
        this.result = result;
        return this;
    }
}

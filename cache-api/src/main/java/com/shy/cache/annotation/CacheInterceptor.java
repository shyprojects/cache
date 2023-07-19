package com.shy.cache.annotation;

import java.lang.annotation.*;

/***
 * 缓存拦截器
 * @author shy
 * @date 2023-07-19 21:38
 */
@Target(ElementType.METHOD)
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInterceptor {
    /**
     * 通用拦截器
     * 1.耗时统计
     * 2.慢日志统计
     * @return true 默认开启
     */
    boolean common() default true;

    /**
     * 是否启用刷新
     * @return false 默认关闭
     */
    boolean refresh() default false;

}

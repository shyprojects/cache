package com.shy.cache.core.util;

/***
 * @author shy
 * @date 2023-07-16 1:48
 */
public class ArgUtil {
    /**
     *
     * @param obj
     * @param name
     */
    public static void notNull(Object obj,String name){
        if (obj == null){
            throw new IllegalArgumentException(name + "不可以为null");
        }
    }
}

package com.shy.cache.core.util;

/**
 * hash工具类
 */
public class HashUtil {
    public static int hash(Object key){
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public static int indexFor(int h, int length){
        return h & (length-1);
    }
}

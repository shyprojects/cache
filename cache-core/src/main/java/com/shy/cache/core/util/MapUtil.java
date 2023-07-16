package com.shy.cache.core.util;

import java.util.Map;

/***
 * @author shy
 * @date 2023-07-16 17:27
 */
public class MapUtil {
    public static boolean isEmpty(Map<?,?> map){
        return map == null || map.size() == 0;
    }
    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }
}

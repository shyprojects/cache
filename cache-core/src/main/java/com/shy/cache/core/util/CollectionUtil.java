package com.shy.cache.core.util;

import java.util.Collection;

/***
 * @author shy
 * @date 2023-07-16 18:07
 */
public class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}

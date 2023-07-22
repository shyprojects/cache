package com.shy.cache.core.support.persist;

import com.shy.cache.api.ICachePersist;

/***
 * 持久化工具类
 * @author shy
 * @date 2023-07-21 15:31
 */
public class CachePersists {

    private CachePersists(){}

    public static <K,V>ICachePersist<K,V> none(){
        return new CachePersistNone<>();
    }
    public static <K,V>ICachePersist<K,V> dbJson(String dbPath){
        return new CachePersistDbJson<>(dbPath);
    }

    public static <K,V> ICachePersist<K,V> aof(String dbPath){
        return new CachePersistAof<>();
    }
}

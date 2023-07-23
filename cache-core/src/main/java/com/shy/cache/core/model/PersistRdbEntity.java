package com.shy.cache.core.model;

import lombok.Data;

/***
 * 持久化明细-rdb
 * @author shy
 * @date 2023-07-21 15:23
 */
@Data
public class PersistRdbEntity<K,V> {
    private K key;

    private V value;

    private Long expire;
}

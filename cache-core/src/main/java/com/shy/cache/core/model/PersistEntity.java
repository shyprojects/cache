package com.shy.cache.core.model;

import lombok.Data;

/***
 * 持久化明细
 * @author shy
 * @date 2023-07-21 15:23
 */
@Data
public class PersistEntity<K,V> {
    private K key;

    private V value;

    private Long expire;
}

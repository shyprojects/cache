package com.shy.cache.core.support.map;

import java.util.*;

public class MyProgressiveReHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {

    /**
     * rehash的下表:
     * -1:代表没有进行rehash
     * !=-1:代表在进行rehash
     */
    private int rehashIndex = -1;

    /**
     * 容量
     * 默认为 8
     */
    private int capacity;

    /**
     * 扩容因子
     * 阈值=容量*factor
     */
    private final double factor = 1.0;

    /**
     * 存放真实数据
     */
    private List<List<Entry<K,V>>> table;

    /**
     * rehash的时候使用，存储信息
     */
    private List<List<Entry<K, V>>> rehashTable;

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}

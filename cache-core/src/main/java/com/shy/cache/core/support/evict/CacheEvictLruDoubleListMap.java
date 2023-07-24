package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.model.DoubleListNode;

import java.util.HashMap;
import java.util.Map;

/***
 * @author shy
 * @date 2023-07-24 19:50
 */
public class CacheEvictLruDoubleListMap<K, V> extends AbstractCacheEvict<K, V> {


    /**
     * 头结点
     */
    private DoubleListNode<K,V> head;

    /**
     * 尾结点
     */
    private DoubleListNode<K,V> tail;

    /**
     * map信息
     */
    private Map<K,DoubleListNode<K,V>> indexMap;


    public CacheEvictLruDoubleListMap(DoubleListNode<K,V> head,DoubleListNode<K,V> tail){
        this.indexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();
        this.head.next(this.tail);
        this.tail.pre(this.head);
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }
}

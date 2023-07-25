package com.shy.cache.core.support.struct.lru.impl;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.core.exception.CacheRuntimeException;
import com.shy.cache.core.model.CacheEntry;
import com.shy.cache.core.model.DoubleListNode;
import com.shy.cache.core.support.struct.lru.ILruMap;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/***
 * @author shy
 * @date 2023-07-25 18:31
 */
public class LruMapDoubleList <K,V> implements ILruMap<K,V> {

    private Logger log = Logger.getLogger(LruMapDoubleList.class);

    /**
     * 头结点
     */
    private DoubleListNode<K,V> head;

    /**
     * 尾结点
     */
    private DoubleListNode<K,V> tail;

    /**
     * map信息:
     * key:元素信息
     * value:元素在list中的结点
     */
    private Map<K,DoubleListNode<K,V>> indexMap;


    public LruMapDoubleList(){
        this.indexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();

        this.head.next(this.tail);
        this.tail.pre(this.head);
    }

    @Override
    public ICacheEntry<K, V> removeEldest() {
        //获取尾结点
        DoubleListNode<K, V> tailPre = this.tail;
        if (tailPre == this.head){
            log.info("当前列表为空，删除失败");
            throw new CacheRuntimeException("当前列表为空，删除失败");
        }
        K key = tailPre.key();
        V value = tail.value();
        removeKey(key);
        return CacheEntry.of(key,value);
    }

    @Override
    public void updateKey(K key) {
        //先移除
        this.removeKey(key);
        //再插入到头结点
        DoubleListNode<K, V> newNode = new DoubleListNode<>();
        newNode.key(key);
        DoubleListNode<K, V> next = this.head.next();
        newNode.next(next);
        next.pre(newNode);
        this.head.next(newNode);
        newNode.pre(this.head);
    }

    /**
     * 移除key
     * @param key
     */
    @Override
    public void removeKey(K key) {
        DoubleListNode<K, V> removeNode = indexMap.get(key);
        if (ObjectUtil.isEmpty(removeNode)){
            return;
        }
        DoubleListNode<K, V> pre = removeNode.pre();
        DoubleListNode<K, V> next = removeNode.next();
        pre.next(next);
        next.pre(pre);
        this.indexMap.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return indexMap.isEmpty();
    }

    @Override
    public boolean contains(K key) {
        return indexMap.containsKey(key);
    }
}

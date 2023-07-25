package com.shy.cache.core.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/***
 * 包含频率之内的信息结点
 * @author shy
 * @date 2023-07-25 23:27
 */
@ToString
@EqualsAndHashCode
public class FreqNode <K,V>{
    /**
     * key
     */
    private K key;

    /**
     * value
     */
    private V value = null;

    /**
     * 频率
     */
    private int frequency = 1;

    public FreqNode<K,V> key(K key){
        this.key = key;
        return this;
    }

    public K key(){
        return this.key;
    }

    public FreqNode<K,V> value(V value){
        this.value = value;
        return this;
    }

    public V value(){
        return this.value;
    }

    public int frequency(){
        return this.frequency;
    }
}

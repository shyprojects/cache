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

    /**
     * 处于rehash状态的容量
     */
    private int rehashCapacity;

    /**
     * 统计容量
     */
    private int size;

    /**
     *
     */
    private final boolean debugMode;


    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    public MyProgressiveReHashMap(){
        this(8);
    }

    public MyProgressiveReHashMap(int capacity){
        this(capacity,false);
    }

    /**
     * 初始化方法
     * @param capacity 容量
     * @param debugMode debug模式是否开启
     */
    public MyProgressiveReHashMap(int capacity,boolean debugMode){
        this.capacity = capacity;
        this.table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            this.table.add(i , new ArrayList<Entry<K, V>>());
        }
        this.rehashTable = null;
        this.rehashIndex = -1;
        this.debugMode = debugMode;
    }

    /**
     * put();
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return
     */
    @Override
    public V put(K key, V value) {
        return null;
    }

    /**
     * 打印table信息
     */
    private void printTable() {
        for(List<Entry<K, V>> list : this.table) {
            for(Entry<K,V> entry : list) {
                System.out.print(entry+ " ") ;
            }
            System.out.println();
        }
    }

    /**
     * 判断是否进行rehash
     * @return
     */
    public boolean isRehash(){
        return this.rehashIndex != -1;
    }

    /**
     * 判断是否需要扩容
     * @return
     */
    public boolean isNeedExpand(){
        double rate = this.size() * 1.0/ this.capacity * 1.0;
        return rate >= factor;
    }
}

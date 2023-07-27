package com.shy.cache.core.support.map;


import com.github.houbb.heaven.reflect.api.IField;
import com.github.houbb.heaven.support.tuple.impl.Pair;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.shy.cache.core.util.HashUtil;
import org.apache.log4j.Logger;

import java.util.*;

public class MyProgressiveReHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V> {

    private final static Logger log = Logger.getLogger(MyProgressiveReHashMap.class);

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

    class DefaultMapEntry implements Map.Entry<K,V>{

        private final K key;
        private V value;

        public DefaultMapEntry(K key,V value){
            this.key = key;
            this.value = value;
        }
        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }
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
        this.rehashCapacity = -1;
    }

    /**
     * put();
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return
     */
    @Override
    public V put(K key, V value) {
        //判断是否正在rehash
        boolean isRehash = this.isRehash();
        if (isRehash){
            //判断这个k-v是否为更新
            Pair<Boolean, V> pair = updateTableInfo(key, value, this.table, this.capacity);
            if (pair.getValueOne()){
                V oldValue = pair.getValueTwo();
                if (debugMode){
                    log.debug("不处于渐进式 rehash，此次为更新操作。key: "+ key +", value: " + value);
                }
                //是更新操作,返回旧值
                return oldValue;
            }else{
                //不是更新操作，插入entry
                return this.createNewEntry(key,value);
            }
        }else{
            //正在进行rehash
            if(debugMode) {
                log.debug("当前处于渐进式 rehash 阶段，额外执行一次渐进式 rehash 的动作");
            }
            rehashToNew();
            Pair<Boolean, V> pair = updateTableInfo(key, value, this.table, this.capacity);
            if(pair.getValueOne()) {
                V oldVal = pair.getValueTwo();
                if(debugMode) {
                    log.debug("此次为更新 table 操作。key: "+ key +", value: "+value);
                    printTable(this.table);
                }
                return oldVal;
            }
            //2.2 是否为 rehashTable 更新
            Pair<Boolean, V> pair2 = updateTableInfo(key, value, this.rehashTable, this.rehashCapacity);
            if(pair2.getValueOne()) {
                V oldVal = pair2.getValueTwo();
                if(debugMode) {
                    log.debug("此次为更新 rehashTable 操作。key: "+ key +", value: "+value);
                    printTable(this.table);
                }
                return oldVal;
            }
        }
        return this.createNewEntry(key,value);
    }

    private V createNewEntry(K key, V value) {
        Entry<K,V> entry = new DefaultMapEntry(key,value);
        int hash = HashUtil.hash(key);
        if (isRehash()){
            int index = HashUtil.indexFor(hash, this.rehashCapacity);
            List<Entry<K,V>> list = this.rehashTable.get(index);
//            if (CollectionUtil.isEmpty(list)){
//                list = new ArrayList<>();
//                this.rehashTable.add(index,list);
//            }
            list.add(entry);
            if(debugMode) {
                log.debug("目前处于 rehash 中，元素直接插入到 rehashTable 中。");
                printTable(this.rehashTable);
            }
        }

        if (isNeedExpand()){
            rehash();
            int index = HashUtil.indexFor(hash, this.rehashCapacity);
            List<Entry<K,V>> list = this.rehashTable.get(index);
            list.add(entry);
            if(debugMode) {
                log.debug("目前处于 rehash 中，元素直接插入到 rehashTable 中。");
                printTable(this.rehashTable);
            }
        }else{
            int index = HashUtil.indexFor(hash, this.capacity);
            List<Entry<K,V>> list = this.table.get(index);
            list.add(entry);
            if(debugMode) {
                log.debug("目前处于 rehash 中，元素直接插入到 rehashTable 中。");
                printTable(this.table);
            }
        }
        this.size++;
        return value;
    }


    private void rehash(){
        if (isRehash()){
            if (debugMode){
                log.info("当前正在rehash，不能重复进行rehash操作");
            }
            return;
        }
        //初始化rehashTale
        this.rehashIndex = -1;
        this.rehashCapacity = 2 * this.capacity;
        this.rehashTable = new ArrayList<>(this.rehashCapacity);
        for (int i = 0; i < capacity; i++) {
            this.rehashTable.add(i,new ArrayList<Entry<K,V>>());
        }
        // 遍历元素第一个元素，其他的进行渐进式更新。
        rehashToNew();
    }

    @Override
    public V get(Object key) {
        if (isRehash()){
            if (debugMode){
                log.info("当前处于渐进式 rehash 状态，额外执行一次操作");
            }
            rehashToNew();
        }
        V result = getValue(key,this.table);
        if (result != null){
            return result;
        }
        if (isRehash()){
            return getValue(key,this.rehashTable);
        }
        return null;
    }


    private V getValue(Object key, List<List<Entry<K, V>>> table) {
        for (List<Entry<K, V>> entryList : table) {
            for (Entry<K, V> entry : entryList) {
                if (ObjectUtil.isNull(key,entry.getKey()) || entry.getKey().equals(key)){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    private void rehashToNew() {
        this.rehashIndex++;

        //判断rehash是否完成
        if (rehashIndex == this.table.size() - 1){
            this.capacity = this.rehashCapacity;
            this.table = this.rehashTable;
            this.rehashIndex = -1;
            this.rehashCapacity = -1;
            this.rehashTable = null;
        }else{
            if (debugMode){
                log.info("渐进式 rehash 处理中, 目前 index："+rehashIndex+" 已完成");
                printAllTable();
            }
        }
    }

    private void printAllTable() {
        System.out.println("table:");
        printTable(this.table);
        System.out.println("rehashTable:");
        printTable(this.rehashTable);
    }

    private Pair<Boolean,V> updateTableInfo(K key, V value, List<List<Entry<K,V>>> table, int tableCapacity){
        //通过hash计算出index
        int hash = HashUtil.hash(key);
        int index = HashUtil.indexFor(hash, tableCapacity);
        //拿到某个下表下到全部链，判断是否为更新操作
        List<Entry<K,V>> entryList = new ArrayList<>();
        if (index < entryList.size()){
            entryList = table.get(index);
        }
        //遍历所有到entry
        for (Entry<K, V> entry : entryList) {
            K entryKey = entry.getKey();
            //entry到key为null 并且key为null 或者 key 和 entry 的key相同，则为替换操作
            if (ObjectUtil.isNull(entryKey,key) || key.equals(entryKey)){
                V oldValue = entry.getValue();
                entry.setValue(value);
                return Pair.of(true,oldValue);
            }
        }
        return Pair.of(false,null);
    }


    /**
     * 打印table信息
     */
    private void printTable(List<List<Entry<K, V>>> table) {
        if (table == null) {
            System.err.println("----");
        }
        if (CollectionUtil.isEmpty(table)){
            return;
        }
        for(List<Entry<K, V>> list : table) {
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

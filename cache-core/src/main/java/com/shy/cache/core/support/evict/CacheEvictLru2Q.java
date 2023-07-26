package com.shy.cache.core.support.evict;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.exception.CacheRuntimeException;
import com.shy.cache.core.model.CacheEntry;
import com.shy.cache.core.model.DoubleListNode;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/***
 * 淘汰策略--lru2q算法
 * @author shy
 * @date 2023-07-25 10:31
 */
public class CacheEvictLru2Q<K,V> extends AbstractCacheEvict<K,V>{

    private final static Logger log = Logger.getLogger(CacheEvictLru2Q.class);

    /**
     * 限制队列大小
     */
    private final static int LIMIT_SIZE_QUEUE = 1024;

    /**
     * 队列
     */
    private Queue<K> firstQueue;

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
    private Map<K,DoubleListNode<K,V>> lruIndexMap;

    public CacheEvictLru2Q(){
        this.firstQueue = new LinkedList<>();
        this.lruIndexMap = new HashMap<>();

        head = new DoubleListNode<>();
        tail = new DoubleListNode<>();
        this.head.next(tail);
        this.tail.pre(head);
    }



    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        ICache<K, V> cache = context.cache();
        if (cache.size() >= context.size()){
            K evictKey = null;
            if (!firstQueue.isEmpty()){
                evictKey = firstQueue.remove();
            } else {
                DoubleListNode<K, V> tailPre = this.tail.pre();
                if (tailPre == this.head){
                    log.error("当前列表为空,无法进行删除");
                    throw new CacheRuntimeException("不可删除头结点");
                }
                evictKey = tailPre.key();
            }
            V evictValue = cache.remove(evictKey);
            result = CacheEntry.of(evictKey,evictValue);
        }
        return result;
    }

    @Override
    public void removeKey(K key) {
        DoubleListNode<K, V> removeNode = lruIndexMap.get(key);
        if (ObjectUtil.isNotNull(removeNode)){
            //移除
            DoubleListNode<K, V> preNode = removeNode.pre();
            DoubleListNode<K, V> nextNode = removeNode.next();
            preNode.next(nextNode);
            nextNode.pre(preNode);
            //删除map中的信息
            lruIndexMap.remove(key);
        }else{
            //不在lruIndexMap,删除队列中的
            firstQueue.remove(key);
        }
    }

    @Override
    public void updateKey(K key) {
        //判断是否在lruIndexMap或者queue中，如果在放在lruIndexMap的头部
        DoubleListNode<K, V> updateNode = lruIndexMap.get(key);
        if (ObjectUtil.isNotNull(updateNode) || firstQueue.contains(key)){
            this.removeKey(key);
            this.addLruMapHead(key);
        }
        //添加到队列
        firstQueue.add(key);
    }

    /**
     * 添加到map首部
     * @param key
     */
    private void addLruMapHead(K key) {
        //创建结点
        DoubleListNode<K, V> node = new DoubleListNode<>();
        node.key(key);
        //添加到链表
        DoubleListNode<K, V> next = this.head.next();
        node.next(next);
        next.pre(node);
        this.head.next(node);
        node.pre(this.head);
        //添加到map
        lruIndexMap.put(key,node);
    }
}

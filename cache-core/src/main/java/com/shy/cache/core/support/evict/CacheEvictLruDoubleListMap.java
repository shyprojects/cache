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
import java.util.Map;


/***
 * lru -- list+hashmap实现
 * @author shy
 * @date 2023-07-24 19:50
 */
public class CacheEvictLruDoubleListMap<K, V> extends AbstractCacheEvict<K, V> {

    private static final Logger log = Logger.getLogger(CacheEvictLruDoubleListMap.class);

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


    public CacheEvictLruDoubleListMap(){
        this.indexMap = new HashMap<>();
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();
        this.head.next(this.tail);
        this.tail.pre(this.head);
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        ICache<K, V> cache = context.cache();
        //超出限制，驱逐
        if (cache.size() >= context.size()){
            //获取头结点的前一个结点
            DoubleListNode<K, V> tailPre = this.tail.pre();
            if (tailPre == this.head){
                //结点为空
                log.error("链表当前为空，不可删除");
                throw new CacheRuntimeException("不可删除头结点！");
            }
            K evictKey = tailPre.key();
            V evictValue = cache.remove(evictKey);
            result = CacheEntry.of(evictKey, evictValue);
        }

        return result;
    }

    @Override
    public void updateKey(K key) {
        //先删除，在插入
        this.removeKey(key);
        //插入这个更新的元素到头部
        DoubleListNode<K, V> node = new DoubleListNode<>();
        node.key(key);

        node.next(this.head.next());
        head.next().pre(node);
        this.head.next(node);
        node.pre(this.head);
        //插入到map中
        indexMap.put(key,node);
    }

    @Override
    public void removeKey(K key) {
        //获取结点
        DoubleListNode<K, V> node = indexMap.get(key);
        //如果为空就结束
        if (ObjectUtil.isEmpty(node)){
            return;
        }
        //如果不为空,删除链表上这个结点
        node.pre().next(node.next());
        node.next().pre(node.pre());
        //删除map中的数据
        this.indexMap.remove(key);
    }
}

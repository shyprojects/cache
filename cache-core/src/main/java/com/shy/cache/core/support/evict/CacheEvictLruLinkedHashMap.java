package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.model.CacheEntry;

import java.util.LinkedHashMap;
import java.util.Map;

/***
 * lru-LinkedHashMap实现
 * @author shy
 * @date 2023-07-25 0:48
 */
public class CacheEvictLruLinkedHashMap <K,V>extends LinkedHashMap<K,V>
        implements ICacheEvict<K,V> {

    /**
     * 是否移除标识
     */
    private volatile boolean removeFlag = false;

    /**
     * 最旧的一个元素
     */
    private transient Map.Entry<K, V> eldest = null;
    public CacheEvictLruLinkedHashMap() {
        super(16, 0.75f, true);
    }
    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        ICache<K, V> cache = context.cache();
        if (cache.size() >= context.size()){
            //驱逐
            removeFlag = true;
            //执行put操作
            super.put(context.key(),null);
            //构建淘汰的元素
            K key = eldest.getKey();
            V value = cache.remove(key);
            result = CacheEntry.of(key,value);
        }else{
            removeFlag = false;
        }
        return result;
    }


    @Override
    public void updateKey(K key) {
        super.put(key,null);
    }

    @Override
    public void removeKey(K key) {
        super.remove(key);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        this.eldest = eldest;
        return removeFlag;
    }

}

package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.List;

/***
 * @author shy
 * @date 2023-07-18 23:13
 */
public class CacheEvictLRU<K, V> extends AbstractCacheEvict<K,V> {

    /**
     * list信息
     */
    private List<K> list = new LinkedList<>();


    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        ICache<K, V> cache = context.cache();
        // 如果超出限制，就移除队尾元素
        if (cache.size() >= context.size()){
            K k = list.get(list.size() - 1);
            V v = cache.remove(k);
            result = CacheEntry.of(k,v);
        }
        return result;
    }

    /**
     * 更新操作，放到首位置
     * @param key
     */
    @Override
    public void update(K key) {
        this.list.remove(key);
        this.list.add(0,key);
    }

    /**
     * 移除操作
     * @param key
     */
    @Override
    public void remove(K key) {
        this.list.remove(key);
    }
}

package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.Queue;

/***
 * @author shy
 * @date 2023-07-12 23:51
 */
public class CacheEvictFIFO<K, V> implements ICacheEvict<K, V> {

    private Queue<K> queue = new LinkedList<>();

    @Override
    public boolean evict(ICacheEvictContext<K, V> context) {
        //CacheEntry result = null;
        boolean result = false;
        ICache<K, V> cache = context.cache();
        // 超过限制，移除
        if (cache.size() >= context.size()) {
            K evictKey = queue.remove();
            V value = cache.remove(evictKey);
            result = true;
//            result = CacheEntry.of(evictKey,value);
        }
        // 将新添加的元素添加到队尾
        final K key = context.key();
        queue.add(key);
        return result;
    }

//    @Override
//    public ICacheEntry<K,V> evict(ICacheEvictContext<K, V> context) {
//        return doEvict(context);
//    }
//
//    @Override
//    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
//        CacheEntry result = null;
//        ICache<K, V> cache = context.cache();
//        // 超过限制，移除
//        if(cache.size() >= context.size()){
//            K evictKey = queue.remove();
//            V value = cache.remove(evictKey);
//            result = CacheEntry.of(evictKey,value);
//        }
//        // 将新添加的元素添加到队尾
//        final K key = context.key();
//        queue.add(key);
//        return result;
//    }


}

package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;

import java.util.LinkedList;
import java.util.Queue;

/***
 * @author shy
 * @date 2023-07-12 23:51
 */
public class CacheEvictFIFO<K, V> implements ICacheEvict<K, V> {

    private Queue<K> queue = new LinkedList<>();

    @Override
    public void evict(ICacheEvictContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        // 超过限制，移除
        if(cache.size() >= context.size()){
            K evictKey = queue.remove();
            cache.remove(evictKey);
        }
        final K key = context.key();
        queue.add(key);
    }
}

package com.shy.cache.core.support.evict;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.model.CacheEntry;
import com.shy.cache.core.support.struct.lru.ILruMap;
import org.apache.log4j.Logger;

/***
 * 淘汰策略--lru2算法
 * @author shy
 * @date 2023-07-25 10:31
 */
public class CacheEvictLru2<K,V> extends AbstractCacheEvict<K,V>{

    private final static Logger log = Logger.getLogger(CacheEvictLru2.class);

    /**
     * 第一次访问的lru
     */
    private ILruMap<K,V> firstLruMap;

    /**
     * 第二次访问的lru
     */
    private ILruMap<K,V> lastLruMap;

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K,V> result = null;
        ICache<K, V> cache = context.cache();
        if (cache.size() >= context.size()){
            ICacheEntry<K,V> evictEntry = null;
            // 如果firstLruMap不为空
            if (!firstLruMap.isEmpty()){
                evictEntry = firstLruMap.removeEldest();
                log.info("从firstLruMap中淘汰数据:"+evictEntry);
            }else{
                //从lastLruMap中移除
                evictEntry = lastLruMap.removeEldest();
                log.info("从lastLruMap中淘汰数据:"+evictEntry);
            }
            //缓存移除操作
            K key = evictEntry.key();
            V value = evictEntry.value();
            result = CacheEntry.of(key,value);
        }
        return result;
    }

    /**
     * 更新key
     * @param key
     */
    @Override
    public void updateKey(K key) {
        if (lastLruMap.contains(key) || firstLruMap.contains(key)){
            //先移除
            this.removeKey(key);
            //再加入到lastLruMap中
            lastLruMap.updateKey(key);
            log.info("多次访问，加入到lastLruMap中"+key);
        }else{
            firstLruMap.updateKey(key);
            log.info("第一次访问，加入到firstLruMap中"+key);
        }
    }

    /**
     * 移除某个元素
     * @param key
     */
    @Override
    public void removeKey(K key) {
        if (lastLruMap.contains(key)){
            lastLruMap.removeKey(key);
            log.info("key:" + key + "从 lastLruMap中移除");
        }else{
            firstLruMap.removeKey(key);
            log.info("key:" + key + "从 firstLruMap");
        }
    }
}

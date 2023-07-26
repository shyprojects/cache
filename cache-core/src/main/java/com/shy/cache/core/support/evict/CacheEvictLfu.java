package com.shy.cache.core.support.evict;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.core.exception.CacheRuntimeException;
import com.shy.cache.core.model.CacheEntry;
import com.shy.cache.core.model.FreqNode;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class CacheEvictLfu<K, V> extends AbstractCacheEvict<K, V> {

    private final Logger log = Logger.getLogger(CacheEvictLfu.class);

    /**
     * key的映射信息
     */
    private final Map<K, FreqNode<K, V>> keyMap;

    /**
     * 某个频率对应的所有key信息，频率相同，被淘汰的应该是后进入的节点
     */
    private final Map<Integer, LinkedHashSet<FreqNode<K, V>>> freqMap;


    /**
     * 最小频率
     */
    private int minFreq;

    public CacheEvictLfu() {
        this.keyMap = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 1;
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        ICacheEntry<K, V> result = null;
        if (cache.size() >= context.size()) {
            //进行驱逐
            FreqNode<K, V> evictNode = this.getMinFreqNode();
            K evictKey = evictNode.key();
            V evictValue = cache.remove(evictKey);
            result = CacheEntry.of(evictKey, evictValue);
            log.debug("淘汰最小频率信息, key: " + evictKey + ", value: " + evictValue + ", freq: " + evictNode.frequency());
        }
        return result;
    }

    private FreqNode<K, V> getMinFreqNode() {
        LinkedHashSet<FreqNode<K, V>> set = this.freqMap.get(minFreq);
        if (CollectionUtil.isNotEmpty(set)) {
            return set.iterator().next();
        }
        throw new CacheRuntimeException("未发现最小频率的key");
    }

    @Override
    public void updateKey(K key) {
        FreqNode<K, V> freqNode = keyMap.get(key);
        if (ObjectUtil.isNotNull(freqNode)){
            //如果存在
            int frequency = freqNode.frequency();
            LinkedHashSet<FreqNode<K, V>> set = freqMap.get(frequency);
            set.remove(freqNode);
            //更新最小频率
            if (frequency == this.minFreq && set.isEmpty()){
                this.minFreq++;
            }
            //频率增加
            frequency++;
            //移除
            freqMap.remove(freqNode.key());
            //添加到新到集合
            FreqNode<K, V> newNode = new FreqNode<>();
            newNode.key(key);
            this.addToFreMap(frequency,newNode);
        }else{
            //如果不存在
            //构建新的元素，添加到lfu中
            FreqNode<K, V> newNode = new FreqNode<>();
            newNode.key(key);
            this.addToFreMap(1,newNode);
            this.keyMap.put(key,newNode);
            this.minFreq = 1;
        }
    }

    private void addToFreMap(int frequency, FreqNode<K, V> newNode) {
        LinkedHashSet<FreqNode<K, V>> set = freqMap.get(frequency);
        if (set == null){
            set = new LinkedHashSet<>();
        }
        set.add(newNode);
        freqMap.put(frequency,set);
    }

    @Override
    public void removeKey(K key) {
        FreqNode<K, V> freqNode = keyMap.remove(key);

        //获取频率
        int frequency = freqNode.frequency();
        LinkedHashSet<FreqNode<K, V>> set = freqMap.get(frequency);
        //移除
        set.remove(freqNode);
        log.info("移除节点:" + freqNode + ",freq=" + frequency);
        //更新minFreq
        if (CollectionUtil.isEmpty(set) && frequency == minFreq) {
            minFreq--;
            log.info("minFreq降低为:" + minFreq);
        }
    }
}

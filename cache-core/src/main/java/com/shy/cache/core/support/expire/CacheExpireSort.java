package com.shy.cache.core.support.expire;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheExpire;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/***
 *
 * 过期策略--排序过期策略
 * @author shy
 * @date 2023-07-26 16:40
 */
public class CacheExpireSort<K, V> implements ICacheExpire<K, V> {

    /**
     * 每次清除的上限
     */
    private static final int LIMIT = 100;

    /**
     * 过期map
     */
    private final Map<K,Long> expireMap = new HashMap<>();
    /**
     *
     */
    private final ICache<K,V> cache;


    /**
     * 按照时间排序
     */
    private final Map<Long, List<K>> sortMap = new TreeMap<>((o1, o2) ->
        (int) (o1 - o2)
    );


    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireSort(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(),100,100, TimeUnit.SECONDS);
    }

    private class ExpireThread implements Runnable{
        @Override
        public void run() {
            if (MapUtil.isEmpty(sortMap)){
                return;
            }
            //处理key
            int count = 0;
            for (Map.Entry<Long, List<K>> entry : sortMap.entrySet()) {
                if (count >= LIMIT){
                    return;
                }
                List<K> expireKeys = entry.getValue();
                Long expireTime = entry.getKey();
                if (CollectionUtil.isEmpty(expireKeys)){
                    return;
                }
                //删除的逻辑
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis >= expireTime){
                    Iterator<K> iterator = expireKeys.iterator();
                    while (iterator.hasNext()){
                        K key = iterator.next();
                        //先移除自身
                        iterator.remove();
                        cache.remove(key);
                        count++;
                    }
                }else {
                    //直接跳过，没有过期信息
                    return;
                }
            }
        }
    }

    @Override
    public void expire(K key, long expireAt) {
        List<K> list = this.sortMap.get(expireAt);
        if (list == null){
            list = new ArrayList<>();
        }
        list.add(key);
        this.sortMap.put(expireAt,list);
        this.expireMap.put(key,expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtil.isEmpty(keyList)){
            return;
        }
        if (keyList.size() <= expireMap.size()){
            for (K key : keyList) {
                //判断是否过期移除
                this.removeExpireKey(key);
            }
        }else{
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                K key = entry.getKey();
                this.removeExpireKey(key);
            }
        }
    }

    private void removeExpireKey(K key) {
        Long expireTime = expireMap.get(key);
        // 存在就移除
        if (expireTime != null){
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis >= expireTime){
                expireMap.remove(key);
                List<K> list = sortMap.get(expireTime);
                list.remove(key);

            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return this.expireMap.get(key);
    }
}

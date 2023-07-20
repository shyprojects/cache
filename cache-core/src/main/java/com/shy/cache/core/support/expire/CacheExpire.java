package com.shy.cache.core.support.expire;

import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheExpire;
import com.shy.cache.api.ICacheRemoveListener;
import com.shy.cache.api.ICacheRemoveListenerContext;
import com.shy.cache.core.constant.enums.CacheRemoveType;
import com.shy.cache.core.support.listener.CacheRemoveListenerContext;
import com.shy.cache.core.util.CollectionUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/***
 *
 * 过期策略--普通策略
 * @author shy
 * @date 2023-07-16 16:17
 */
public class CacheExpire<K, V> implements ICacheExpire<K, V> {

    /**
     * 每次清除的上线
     */
    private static final int LIMIT = 100;

    /**
     * 过期map
     * K:key
     * Long:过期时间
     * 空间换时间
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 缓存实现
     */
    private final ICache<K, V> cache;

    /**
     * 线程池执行
     */
    private static final ScheduledExecutorService EXECUTOR_SERVICE
            // 单线程周期性或者定时执行任务
            = Executors.newSingleThreadScheduledExecutor();

    public CacheExpire(ICache<K, V> cache) {
        this.cache = cache;
        this.init();
    }

    /**
     * 初始化线程池任务
     */
    public void init() {
        EXECUTOR_SERVICE.scheduleWithFixedDelay(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行定时任务
     */
    public class ExpireThread implements Runnable {

        @Override
        public void run() {
            // 1.判断是否为空
            if (expireMap.isEmpty()) {
                return;
            }
            // 2.获取 key 进行处理
            int count = 0;
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                if (count > LIMIT) {
                    return;
                }
                expireKey(entry.getKey());
                count++;
            }
        }
    }

    /**
     * 添加某个key及其过期时间
     * @param key 过期的key
     * @param expireAt 什么时候过期
     */
    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    /**
     * 惰性删除需要处理的key
     * @param keyList
     */
    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtil.isEmpty(keyList)) {
            return;
        }
        // 判断大小，小的作为外循环。一般都是过期的keys比较小
        if (keyList.size() <= expireMap.size()){
            for (K key : keyList){
                // 这里直接删除cache中的map即可
                this.expireKey(key);
            }
        }else{
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                // 这里需要删除cache和存放过期时间的map中的元素
                this.expireKey(entry.getKey());
            }
        }
    }

    /**
     * 通过entry过期处理key
     * @param entry
     */
    @Deprecated
    private void expireKey(Map.Entry<K, Long> entry) {
        // 获取过期时间
        K key = entry.getKey();
        Long expireAt = entry.getValue();
        if (expireAt == null) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        // 如果过期了
        if (expireAt <= currentTime) {
            expireMap.remove(key);
            cache.remove(key);
        }
    }

    /**
     * 通过key过期处理key
     * @param key
     */
    private void expireKey(K key) {
        // 获取过期时间
        Long expireAt = expireMap.get(key);
        if (expireAt == null) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        // 如果过期了
        if (expireAt <= currentTime) {
            expireMap.remove(key);
            //再移除缓存中的
            V removeValue = cache.remove(key);
            ICacheRemoveListenerContext<K,V> context = CacheRemoveListenerContext.<K,V>newInstance().key(key).value(removeValue).type(CacheRemoveType.EXPIRE.code());
            for (ICacheRemoveListener<K, V> removeListener : cache.removeListeners()) {
                removeListener.listen(context);
            }
        }
    }
}

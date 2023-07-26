package com.shy.cache.core.support.expire;

import com.github.houbb.heaven.util.util.CollectionUtil;
import com.github.houbb.heaven.util.util.MapUtil;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheExpire;
import com.shy.cache.api.ICacheRemoveListener;
import com.shy.cache.api.ICacheRemoveListenerContext;
import com.shy.cache.core.constant.enums.CacheRemoveType;
import com.shy.cache.core.support.listener.remove.CacheRemoveListenerContext;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
/***
 *
 * 过期策略--随机过期策略
 * @author shy
 * @date 2023-07-26 16:40
 */
public class CacheExpireRandom<K, V> implements ICacheExpire<K, V> {

    private Logger log = Logger.getLogger(CacheExpireRandom.class);

    /**
     * 每次清除的大小限制
     */
    private static final int SIZE_LIMIT = 100;

    /**
     * 缓存实现
     */
    private ICache<K, V> cache;

    /**
     * 过期map
     */
    private final Map<K, Long> expireMap = new HashMap<>();

    /**
     * 是否启用快速模式
     */
    private volatile boolean fastMode = false;


    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireRandom(ICache<K, V> cache) {
        this.cache = cache;
        init();
    }

    private void init() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThreadRandom(), 10, 10, TimeUnit.SECONDS);
    }

    private class ExpireThreadRandom implements Runnable {
        @Override
        public void run() {
            if (MapUtil.isEmpty(expireMap)){
                log.info("expireMap 信息为空，直接跳过本次处理。");
                return;
            }
            //快速模式
            if (fastMode){
                expireKeys(10L);
            }
            //缓慢模式
            expireKeys(100l);
        }


    }
    private void expireKeys(long timeoutMills) {
        //设置超时时间
        long timeLimit = System.currentTimeMillis() + timeoutMills;
        //恢复fastMode
        fastMode = false;
        //获取key进行处理
        int count = 0;
        while (true){
            //判断是否超过100限制
            if (count >= SIZE_LIMIT){
                log.info("过期淘汰次数已经达到最大次数: {}，完成本次执行。"+ SIZE_LIMIT);
                return;
            }
            if (System.currentTimeMillis() >= timeLimit){
                //超时结束
                this.fastMode = true;
                log.info("过期淘汰已经达到限制时间，中断本次执行，设置 fastMode=true;");
                return;
            }
            //随机过期一个
            count++;
            K key = this.getRandomKey();
            Long expireTime = this.expireMap.get(key);
            boolean expireFlag = this.expireKey(key, expireTime);
            log.info("key: "+key+" 过期执行结果 "+ expireFlag);
        }

    }

    /**
     * 随机获取一个key
     * @return
     */
    private K getRandomKey() {
        Random random = ThreadLocalRandom.current();
        Set<K> set = expireMap.keySet();
        List<K> list = new ArrayList<>(set);
        int keyIndex = random.nextInt(list.size());
        return list.get(keyIndex);
    }

    @Override
    public void expire(K key, long expireAt) {
        expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if (CollectionUtil.isEmpty(keyList)) {
            //没有淘汰过期的key
            return;
        }
        //判断大小，遍历小的即可
        if (keyList.size() <= expireMap.size()) {
            for (K k : keyList) {
                this.expireKey(k, expireMap.get(k));
            }
        } else {
            for (Map.Entry<K, Long> entry : expireMap.entrySet()) {
                this.expireKey(entry.getKey(), entry.getValue());
            }
        }
    }

    private boolean expireKey(K key, Long expireAt) {
        if (expireAt == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
            //过期
            expireMap.remove(key);
            V expireValue = cache.remove(key);
            //执行淘汰监听器
            List<ICacheRemoveListener<K, V>> removeListeners = cache.removeListeners();
            for (ICacheRemoveListener<K, V> removeListener : removeListeners) {
                ICacheRemoveListenerContext<K, V> context = CacheRemoveListenerContext
                        .<K, V>newInstance()
                        .key(key)
                        .value(expireValue)
                        .type(CacheRemoveType.EXPIRE.code());
                removeListener.listen(context);
            }
        }
        return false;
    }

    @Override
    public Long expireTime(K key) {
        return expireMap.get(key);
    }
}

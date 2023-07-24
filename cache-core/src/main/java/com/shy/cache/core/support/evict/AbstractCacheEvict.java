package com.shy.cache.core.support.evict;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.shy.cache.api.ICacheEntry;
import com.shy.cache.api.ICacheEvict;
import com.shy.cache.api.ICacheEvictContext;
import com.shy.cache.api.ICacheRemoveListener;
import com.shy.cache.core.constant.enums.CacheRemoveType;
import com.shy.cache.core.support.listener.remove.CacheRemoveListenerContext;

import java.util.List;

/***
 * @author shy
 * @date 2023-07-18 22:15
 */
public abstract class AbstractCacheEvict<K, V> implements ICacheEvict<K, V> {
    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEntry = doEvict(context);
        if (ObjectUtil.isNotNull(cacheEntry)){
            CacheRemoveListenerContext<K, V> removeListenerContext =
                    CacheRemoveListenerContext.<K, V>newInstance()
                            .key(cacheEntry.key())
                            .value(cacheEntry.value())
                            .type(CacheRemoveType.EVICT.code());
            List<ICacheRemoveListener<K, V>> removeListeners = context.cache().removeListeners();
            // 遍历删除监听器
            for (ICacheRemoveListener<K, V> removeListener : removeListeners) {
                removeListener.listen(removeListenerContext);
            }
        }
        return cacheEntry;
    }

    /**
     * 执行移除的方法
     * @param context
     * @return
     */
    protected abstract ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context);

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}

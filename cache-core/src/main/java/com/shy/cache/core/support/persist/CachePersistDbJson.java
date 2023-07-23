package com.shy.cache.core.support.persist;

import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.shy.cache.api.ICache;
import com.shy.cache.core.model.PersistRdbEntity;

import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/***
 * 持久化 db -- 基于json
 * @author shy
 * @date 2023-07-21 15:34
 */
public class CachePersistDbJson<K, V> extends CachePersistAdaptor<K,V> {

    /**
     * 持久化存储的位置
     */
    private final String dbPath;

    public CachePersistDbJson(String dbPath){
        this.dbPath = dbPath;
    }
    @Override
    public void persist(ICache<K, V> cache) {
        Set<Map.Entry<K, V>> entries = cache.entrySet();

        //创建文件
        FileUtil.createFile(dbPath);
        //清空文件
        FileUtil.truncate(dbPath);
        for (Map.Entry<K, V> entry : entries) {
            PersistRdbEntity<K, V> persistEntity = new PersistRdbEntity<>();
            K key = entry.getKey();
            persistEntity.setKey(key);
            persistEntity.setValue(entry.getValue());
            persistEntity.setExpire(cache.expire().expireTime(key));

            String JSONObject = JSON.toJSONString(persistEntity);
            //StandardOpenOption.APPEND 追加策略
            FileUtil.write(dbPath,JSONObject, StandardOpenOption.APPEND);
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUtil() {
        return TimeUnit.MINUTES;
    }
}

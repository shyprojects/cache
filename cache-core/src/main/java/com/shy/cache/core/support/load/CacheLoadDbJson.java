package com.shy.cache.core.support.load;

import com.alibaba.fastjson2.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheLoad;
import com.shy.cache.core.model.PersistEntity;
import org.apache.log4j.Logger;

import java.util.List;

public class CacheLoadDbJson<K, V> implements ICacheLoad<K, V> {

    private final Logger log = Logger.getLogger(CacheLoadDbJson.class);

    private final String dbPath;

    public CacheLoadDbJson(String dbPath){
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        List<String> lines = FileUtil.readAllLines(dbPath);
        log.info("「load」开始处理path = " + dbPath);
        if (CollectionUtil.isEmpty(lines)) {
            log.info("「load」开始处理 path = " + dbPath);
            return;
        }
        for (String line : lines) {
            if (StringUtil.isEmpty(line)){
                continue;
            }
            PersistEntity<K,V> entity = JSON.parseObject(line, PersistEntity.class);
            K key = entity.getKey();
            V value = entity.getValue();
            Long expire = entity.getExpire();
            cache.put(key,value);
            if (ObjectUtil.isNotEmpty(expire)){
                cache.expireAt(key,expire);
            }
        }
    }
}

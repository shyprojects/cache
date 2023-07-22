package com.shy.cache.core.support.persist;

import com.github.houbb.heaven.util.io.FileUtil;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICachePersist;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachePersistAof<K,V> implements ICachePersist<K,V> {

    private final Logger log = Logger.getLogger(CachePersistAof.class);
    /**
     * 数据持久化路径
     */
    private final String dbPath;

    /**
     * 缓存列表
     */
    private final List<String> bufferList = new ArrayList<>();

    public CachePersistAof(String dbPath){
        this.dbPath = dbPath;
    }
    @Override
    public void persist(ICache<K, V> cache) {
        log.info("开始 AOF 持久化到文件");
        //1.创建文件
        if (FileUtil.isEmpty(dbPath)) {
            FileUtil.createFile(dbPath);
        }
        //2.持久化追加
        FileUtil.append(dbPath,bufferList);
        // TODO
    }

    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUtil() {
        return TimeUnit.SECONDS;
    }
}

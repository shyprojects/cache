package com.shy.cache.core.support.persist;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.shy.cache.api.ICache;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/***
 * 持久化方案-aof
 * @author shy
 * @date 2023-07-22 23:36
 */
public class CachePersistAof<K, V> extends CachePersistAdaptor<K, V> {

    private final Logger log = Logger.getLogger(CachePersistAof.class);
    /**
     * 数据持久化路径
     */
    private final String dbPath;

    /**
     * 缓存列表
     */
    private final List<String> bufferList = new ArrayList<>();

    public CachePersistAof(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void persist(ICache<K, V> cache) {
        log.info("AOF 持久化--写入缓存--开始");
        //1.创建文件
        if (FileUtil.isEmpty(dbPath)) {
            FileUtil.createFile(dbPath);
        }
        //2.持久化追加
        FileUtil.append(dbPath, bufferList);
        //3.清空buffer
        bufferList.clear();
        log.info("AOF 持久化--写入缓存--结束");
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

    /**
     * 追加aof
     * @param json
     */
    public void append(final String json){
        if (StringUtil.isNotEmpty(json)) {
            bufferList.add(json);
        }
    }
}

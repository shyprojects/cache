package com.shy.cache.core.support.load;

import com.alibaba.fastjson.JSON;
import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.heaven.util.lang.reflect.ReflectMethodUtil;
import com.github.houbb.heaven.util.util.CollectionUtil;
import com.shy.cache.annotation.CacheInterceptor;
import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheLoad;
import com.shy.cache.core.core.Cache;
import com.shy.cache.core.model.PersistAofEntity;
import org.apache.log4j.Logger;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 加载策略-aof
 * @author shy
 * @date 2023-07-23 19:17
 */
public class CacheLoadAof<K, V> implements ICacheLoad<K, V> {

    private final Logger log = Logger.getLogger(CacheLoadAof.class);
    /**
     * 方法缓存，通过方法名直接获取方法
     */
    private static final Map<String, Method> METHOD_MAP = new HashMap<>();

    /**
     * 加载的aof文件的路径
     */
    private final String dbPath;

    static {
        Method[] methods = Cache.class.getMethods();
        for (Method method : methods) {
            CacheInterceptor cacheInterceptor = method.getAnnotation(CacheInterceptor.class);
            // 有这个注解，并且注解的aof属性为true
            if (cacheInterceptor != null && cacheInterceptor.aof()) {
                METHOD_MAP.put(method.getName(),method);
            }
        }
    }
    public CacheLoadAof(String dbPath){
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        List<String> lines = FileUtil.readAllLines(dbPath);
        log.info("[load] 开始处理 path:" + dbPath);
        if (CollectionUtil.isEmpty(lines)){
            log.info("文件为空，处理完毕");
            return;
        }
        // 遍历每一行
        for (String line : lines) {
            if (StringUtil.isEmpty(line)){
                continue;
            }
            // 反序列化
            PersistAofEntity entity = JSON.parseObject(line, PersistAofEntity.class);
            // 获取方法参数
            Method method = METHOD_MAP.get(entity.getMethodName());
            Object[] params = entity.getParams();
            // 反射调用
            ReflectMethodUtil.invoke(cache,method,params);
        }
    }
}

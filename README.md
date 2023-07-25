# 项目简介

Cache是一款可扩展的本地缓存框架

## 特性

- fluent 流式写法，使编程更加优雅
- 支持缓存expire过期策略
- 支持自定义删除监听器
- 多种evict驱逐策略
- 支持持久化功能
- 提供load加载器，加载持久化文件中的内容

# 快速开始

JDK1.7 及其以上版本

Maven 3.X 及其以上版本

## maven依赖

```xml
        <dependency>
            <groupId>com.shy</groupId>
            <artifactId>cache-core</artifactId>
            <version>{版本}</version>
        </dependency>
```

## 基本测试

```java
	    ICache<String, String> cache = CacheBs.<String,String>newInstance()
                // 指定缓存大小
                .size(2)
                // 指定驱逐策略
                .evict(new CacheEvictFIFO<>())
                .build();
        cache.put("k1","v1");
        cache.put("k2","v2");
        cache.put("k3","v3");
        System.out.println(cache.get("k1"));
        System.out.println(cache.get("k2"));
        System.out.println(cache.get("k3"));
        System.out.println(cache.size());
        System.out.println(cache.keySet());
```

**驱逐策略：**先进先出，故输出的结果为：

```
null
v2
v3
2
[k2, k3]
```

## 使用引导类配置属性

`CacheBs`作为缓存的引导类，支持fluent写法，编程更加优雅

```java
        ICache<String, String> cache = CacheBs.<String, String>newInstance()
                .evict(CacheEvicts.<String,String>fifo())
                .size(2)
                .build();
```

# 过期支持

```java
	   ICache<String, String> cache = CacheBs.<String, String>newInstance()
                .evict(CacheEvicts.<String,String>fifo())
                .size(2)
                .build();
        cache.put("k1","v1");
        cache.put("k2","v2");
        // 给k1设置过期时间1s
        cache.expire("k1",100);
        TimeUnit.SECONDS.sleep(1);
        System.out.println(cache.keySet());
```



# 淘汰策略

目前内置了几种淘汰策略，可以直接通过 `CacheEvicts` 工具类创建。

| 淘汰策略         | 说明                                                     |
| ---------------- | -------------------------------------------------------- |
| none             | 无淘汰策略，超出限制直接抛出异常                         |
| fifo             | 先进先出淘汰策略                                         |
| lru              | 最基本的lru策略                                          |
| lruDoubleListMap | 基于双向链表+MAP 实现的LRU，性能优于 lru                 |
| lruLinkedHashMap | 基于 LinkedHashMap 实现的LRU，与 lruDoubleListMap 差不多 |
| lru2Q            | 基于 LRU 2Q 的改进版 LRU 实现，命中率优于LRU             |
| lru2             | 基于 LRU-2 的改进版 LRU 实现，命中率优于 lru2Q           |

# 慢日志监听器

**说明：**redis会存储慢操作相关日志，主要是由两个参数构成：

* slowly-log-slower-than：预设阈值,它的单位是毫秒(1秒=1000000微秒)默认值是10000
* slowlog-max-len 最多存储多少条的慢日志记录

不过 redis 是直接存储到内存中，而且有长度限制。

根据实际工作体验，如果我们可以添加慢日志的监听，然后有对应的存储或者报警，这样更加方便问题的分析和快速反馈。

所以我们引入类似于删除的监听器。

**自定义慢操作监听器：**

```java
public class MyCacheSlowListener<K,V> implements ICacheSlowListener<K,V> {

    private final Logger log = Logger.getLogger(MyCacheSlowListener.class);

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn("[慢日志]" + context.methodName());
    }

    @Override
    public long slowerThanMills() {
        return 0l;
    }
}
```

**使用：**

```java
ICache<String, String> cache = CacheBs.<String, String>newInstance()
                .size(2)
                .addSlowListener(new MyCacheSlowListener<>())
                .build();
        cache.put("k1","v1");
        System.out.println(cache.get("k1"));
```

**效果：**

```
[INFO]73 com.shy.cache.core.support.interceptor.common.CacheInterceptorTimeCostmain2023-07-22 15:44:24:280 Cost end,method:put,cost:1ms
[WARN]74 MyCacheSlowListenermain2023-07-22 15:44:24:281 [慢日志]put
[INFO]75 com.shy.cache.core.support.interceptor.common.CacheInterceptorTimeCostmain2023-07-22 15:44:24:282 Cost start, method:get
[INFO]75 com.shy.cache.core.support.interceptor.common.CacheInterceptorTimeCostmain2023-07-22 15:44:24:282 Cost end,method:get,cost:0ms
[WARN]75 MyCacheSlowListenermain2023-07-22 15:44:24:282 [慢日志]get
v1
```



# 删除监听器

```java
public class MyCacheRemoveListener<K,V> implements ICacheRemoveListener<K,V> {

    final Logger log = Logger.getLogger(MyCacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.info("删除key");
    }
}
```

```java
    public static void testCache() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                // 指定缓存大小
                .size(2)
                // 指定驱逐策略
                .evict(new CacheEvictFIFO<>())
                .addRemoveListener(new MyCacheRemoveListener<>())
                .build();
        cache.put("k1","v1");
        cache.put("k2","v2");
        cache.put("k3","v3");
        System.out.println(cache.keySet());
    }
```

```properties
# 日志
[INFO]3 MyCacheRemoveListenermain2023-07-20 19:30:42:674 删除key
```

# 添加load加载器

实现`ICacheLoad`接口即可

```java
public class MyCacheLoad implements ICacheLoad<String,String> {
    @Override
    public void load(ICache<String,String> cache) {
        cache.put("k0","v0");
    }
}
```

调用`load()`添加该加载器

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
                // 指定缓存大小
                .size(2)
                // 指定驱逐策略
                .evict(new CacheEvictFIFO<>())
                .load(new MyCacheLoad())
                .addRemoveListener(new CacheListener<>())
                .build();
        cache.put("k1","v1");
        System.out.println(cache.keySet());
```

结果：加载器中的数据已经加载

```
[k0, k1]
```

# 添加了持久化功能

调用`persist()`开启持久化功能，使用CachePersistDbJson每隔10s就会将内存中的数据进行一次持久化

```java
ICache<String, String> cache = CacheBs.<String,String>newInstance()
        // 指定缓存大小
        .size(2)
        // 指定驱逐策略
        .evict(new CacheEvictFIFO<>())
        .load(new MyCacheLoad())
        .addRemoveListener(new CacheListener<>())
        .persist(new CachePersistDbJson<String,String>("1.rdb"))
        .build();
cache.put("k2","v3");
```
使用load加载器`CacheLoadDbJson`可以将持久化的文件加载到内存中

```java
    @Test
    public void testLoadDbJson() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                // 指定缓存大小
                .size(2)
                // 指定驱逐策略
                .evict(new CacheEvictFIFO<>())
                .load(new CacheLoadDbJson<>("1.rdb"))
                .build();
        TimeUnit.SECONDS.sleep(5);
        System.err.println(cache.keySet());
    }
```

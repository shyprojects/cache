# 项目简介

Cache是一款可扩展的本地缓存框架

## 特性

- fluent 流式写法，使编程更加优雅
- 支持缓存expire过期策略
- 支持自定义删除监听器
- 多种evict驱逐策略

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

<<<<<<< HEAD
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
=======
>>>>>>> 3ca48786fb75a14cb9d622a8ca40cf56387dadc7

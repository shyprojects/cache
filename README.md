# 项目简介

Cache是一款可扩展的本地缓存框架

**驱逐策略：**

* 先进先出
* 不驱逐，当超过缓存大小时抛出异常

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

## 过期支持

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


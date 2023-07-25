import com.shy.cache.api.ICache;
import com.shy.cache.core.bs.CacheBs;
import com.shy.cache.core.support.evict.CacheEvictFIFO;
import com.shy.cache.core.support.evict.CacheEvicts;
import com.shy.cache.core.support.load.CacheLoadAof;
import com.shy.cache.core.support.load.CacheLoadDbJson;
import com.shy.cache.core.support.persist.CachePersistDbJson;
import com.shy.cache.core.support.persist.CachePersists;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/***
 * @author shy
 * @date 2023-07-12 0:34
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        testCache();
//        testExpire();
    }



    @Test
    public void testSlowListener(){
        ICache<String, String> cache = CacheBs.<String, String>newInstance()
                .size(2)
                .addSlowListener(new MyCacheSlowListener<>())
                .build();
        cache.put("k1","v1");
        System.out.println(cache.get("k1"));
    }

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
    @Test
    public void testPersist() throws InterruptedException {
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
        cache.put("k1","v1");
    }
    public static void testCache() throws InterruptedException {
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
    }

    public static void testExpire() throws InterruptedException {
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
    }

    @Test
    public void testAofPersist() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .persist(CachePersists.aof("1.aof"))
                .size(2)
                .build();
        TimeUnit.SECONDS.sleep(10);
        cache.put("k1","v1");
        System.out.println(cache.keySet());
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    public void testLoadAof() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .load(new CacheLoadAof<>("1.aof"))
                .size(2)
                .build();
        System.out.println(cache.keySet());
    }

    @Test
    public void testLru(){
        ICache<String, String> cache = CacheBs.<String, String>newInstance()
                .evict(CacheEvicts.lru())
                .size(2)
                .build();
        cache.put("k1","v1");
        cache.put("k2","v2");
        System.out.println(cache.get("k1"));
        cache.put("k3","v3");
        System.out.println(cache.keySet());
    }
    @Test
    public void lruDoubleListMapTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lruDoubleListMap())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }
    @Test
    public void lruLinkedHashMapTest() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lruLinkedHashMap())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    @Test
    public void lru2Test()  {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lru2())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

    @Test
    public void lruQ2Test()  {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                .size(3)
                .evict(CacheEvicts.<String, String>lru2Q())
                .build();

        cache.put("A", "hello");
        cache.put("B", "world");
        cache.put("C", "FIFO");

        // 访问一次A
        cache.get("A");
        cache.put("D", "LRU");

        Assert.assertEquals(3, cache.size());
        System.out.println(cache.keySet());
    }

}

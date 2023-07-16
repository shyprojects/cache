import com.shy.cache.api.ICache;
import com.shy.cache.core.bs.CacheBs;
import com.shy.cache.core.support.evict.CacheEvictFIFO;
import com.shy.cache.core.support.evict.CacheEvicts;

import java.util.concurrent.TimeUnit;

/***
 * @author shy
 * @date 2023-07-12 0:34
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
//     testCache();
        testExpire();
    }

    public static void testCache() throws InterruptedException {
        ICache<String, String> cache = CacheBs.<String,String>newInstance()
                // 指定缓存大小
                .size(2)
                // 指定驱逐策略
                .evict(new CacheEvictFIFO<>())
                .build();
        cache.put("k1","v1");
        cache.put("k2","v2");
        cache.put("k3","v3");
        cache.expire("k2",10);
        TimeUnit.SECONDS.sleep(1);
        System.out.println(cache.size());
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

}

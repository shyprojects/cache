import com.shy.cache.api.ICacheRemoveListener;
import com.shy.cache.api.ICacheRemoveListenerContext;

/***
 * @author shy
 * @date 2023-07-21 0:29
 */
public class CacheListener<K,V> implements ICacheRemoveListener<K,V> {
    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        System.err.println("...");
    }
}

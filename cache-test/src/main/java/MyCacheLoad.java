import com.shy.cache.api.ICache;
import com.shy.cache.api.ICacheLoad;

public class MyCacheLoad implements ICacheLoad<String,String> {
    @Override
    public void load(ICache<String,String> cache) {
        cache.put("k0","v0");
    }
}

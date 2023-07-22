import com.shy.cache.api.ICacheSlowListener;
import com.shy.cache.api.ICacheSlowListenerContext;
import org.apache.log4j.Logger;

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

package gestao.config.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.cache.concurrent.ConcurrentMapCache;

import io.micrometer.core.lang.Nullable;

/**
 * Classe responsável pelas métricas de cache.
 * 
 * @author edmilson.santana
 *
 */
public class ConcurrentMapCacheMetricsWrapper extends ConcurrentMapCache {

	private final AtomicLong hitCount = new AtomicLong(0);
	private final AtomicLong missCount = new AtomicLong(0);
	private final AtomicLong putCount = new AtomicLong(0);
	private final AtomicLong evictCount = new AtomicLong(0);

	public ConcurrentMapCacheMetricsWrapper(String name) {
		super(name);
	}

	@Override
	public ValueWrapper get(Object key) {
		countGet(key);
		return super.get(key);
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		countGet(key);
		return super.get(key, type);
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		countGet(key);
		return super.get(key, valueLoader);
	}

	@Override
	public void put(Object key, Object value) {
		putCount.incrementAndGet();
		super.put(key, value);
	}

	@Override
	public void clear() {
		super.clear();
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		if (!getNativeCache().containsKey(key)) {
			putCount.incrementAndGet();
		}
		return super.putIfAbsent(key, value);
	}

	@Override
	public void evict(Object key) {
		evictCount.incrementAndGet();
		super.evict(key);
	}

	@Nullable
	private ValueWrapper countGet(Object key) {
		ValueWrapper valueWrapper = super.get(key);
		if (valueWrapper != null) {
			hitCount.incrementAndGet();
		} else {
			missCount.incrementAndGet();
		}
		return valueWrapper;
	}

	public long getHitCount() {
		return hitCount.get();
	}

	public long getMissCount() {
		return missCount.get();
	}

	public long getPutCount() {
		return putCount.get();
	}

	public long getEvictCount() {
		return evictCount.get();
	}

}

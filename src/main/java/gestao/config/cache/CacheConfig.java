package gestao.config.cache;

import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig extends CachingConfigurerSupport {
	
	private final CacheProperties properties;
	
	public CacheConfig(CacheProperties properties) {
		this.properties = properties;
	}
	
	@Bean
	@Override
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		
		cacheManager.setCaches(properties.getCacheNames()
				.stream().map(ConcurrentMapCacheMetricsWrapper::new)
				.collect(Collectors.toList()));
		
		return cacheManager;
	}

}

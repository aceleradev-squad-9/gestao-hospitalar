package gestao.config.cache;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cache")
public class CacheProperties {

	private List<String> cacheNames;

	public List<String> getCacheNames() {
		return cacheNames;
	}

	public void setCacheNames(List<String> cacheNames) {
		this.cacheNames = cacheNames;
	}
}

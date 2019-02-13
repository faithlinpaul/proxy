package in.co.ee.proxy.config;

import in.co.ee.proxy.exception.ConfigurationException;
import in.co.ee.proxy.httpclient.ProxiedHostConfiguration;
import lombok.Data;
import org.apache.commons.collections.map.UnmodifiableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties
@Data
public class ProvidersConfig {
    private Map<String, ProviderConfig> providers;
    @Autowired
    private DefaultConfig defaultConfig;
    @Autowired
    private CacheConfig cache;

    @PostConstruct
    public void init() {
        providers.forEach((providerName,providerProperties)-> {
            providerProperties.setDefaultConfig(defaultConfig);
        });
    }

    public Map<String, ProxiedHostConfiguration> toServiceEndpointConfigurations() {
        if (providers.isEmpty()) {
            throw new ConfigurationException("There should be at least one provider configured in application.yml");
        }

        final HashMap<String, ProxiedHostConfiguration> serviceEndpointConfigurations = new HashMap<>();
        providers.forEach((providerName,providerProperties)-> {
            serviceEndpointConfigurations.put(providerName, new ProxiedHostConfiguration(providerName, providerProperties));
        });
        return UnmodifiableMap.decorate(serviceEndpointConfigurations);
    }

    public ProxiedHostConfiguration getEndpointConfiguration(String providerName) {
        return toServiceEndpointConfigurations().get(providerName);
    }

    public ProviderType getProviderType(String providerName) {
        return providers.get(providerName).getProviderType();
    }

    public ProviderConfig getProviderConfig(String providerName) {
        return providers.get(providerName);
    }



}

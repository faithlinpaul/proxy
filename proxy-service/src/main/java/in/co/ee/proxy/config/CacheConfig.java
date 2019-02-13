package in.co.ee.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="cache")
@Data
public class CacheConfig {
    private String host;
    private Integer port;
    private Integer threadPoolSize;
    private Integer timeout;
}

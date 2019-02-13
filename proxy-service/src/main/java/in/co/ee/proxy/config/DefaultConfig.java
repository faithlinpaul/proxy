package in.co.ee.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="default")
@Data
public class DefaultConfig {
    private Integer connectionPoolMaxTotal;
    private Integer connectionPoolMaxPerRoute;
    private Integer cacheDurationSecs;
    private Integer requestTimeoutMs;
    private Integer connectTimeoutMs;
    private int cacheThreadPoolSize;
}

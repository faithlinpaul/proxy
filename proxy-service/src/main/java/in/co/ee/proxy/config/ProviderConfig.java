package in.co.ee.proxy.config;

import lombok.Data;


@Data
public class ProviderConfig {
    private DefaultConfig defaultConfig;

    //TODO need to add not null rule
    private ProviderType providerType;
    private String url;
    private String keystoreBaseName;
    private String keystorePassword;
    private String truststoreBaseName;
    private String truststorePassword;
    private Integer connectionPoolMaxPerRoute;
    private Integer connectionPoolMaxTotal;
    private Integer requestTimeoutMs;
    private Integer connectTimeoutMs;
    private Integer cacheDurationSecs;

    public Integer getConnectionPoolMaxPerRoute() {
        return connectionPoolMaxPerRoute != null ? connectionPoolMaxPerRoute : defaultConfig.getConnectionPoolMaxPerRoute();
    }

    public Integer getRequestTimeoutMs() {
        return requestTimeoutMs != null ? requestTimeoutMs : defaultConfig.getRequestTimeoutMs();
    }

    public Integer getConnectTimeoutMs() {
        return connectTimeoutMs != null ? connectTimeoutMs : defaultConfig.getConnectTimeoutMs();
    }

    public Integer getConnectionPoolMaxTotal() {
        return connectionPoolMaxTotal != null ? connectionPoolMaxTotal : defaultConfig.getConnectionPoolMaxTotal();
    }

    public Integer getCacheDurationSecs() {
        return cacheDurationSecs != null ? cacheDurationSecs : defaultConfig.getCacheDurationSecs();
    }

    public void setDefaultConfig(DefaultConfig defaultConfig) {
        this.defaultConfig = defaultConfig;
    }



}

package in.co.ee.proxy.httpclient;

import in.co.ee.proxy.config.ProviderConfig;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import in.co.ee.proxy.config.ProvidersConfig;

import static org.apache.http.impl.client.HttpClientBuilder.create;
import static org.apache.http.ssl.SSLContexts.custom;

public class HttpClientFactory {

    private ClientKeystoreBuilder clientKeystoreBuilder;
    private TruststoreBuilder truststoreBuilder;
    private final ProvidersConfig providersConfig;

    public HttpClientFactory(ClientKeystoreBuilder clientKeystoreBuilder, TruststoreBuilder truststoreBuilder,
                             ProvidersConfig providersConfig) {
        this.clientKeystoreBuilder = clientKeystoreBuilder;
        this.truststoreBuilder = truststoreBuilder;
        this.providersConfig = providersConfig;
    }

    public CloseableHttpClient newInstance(ProxiedHostConfiguration endPoint) {
        ProviderConfig providerConfig = providersConfig.getProviderConfig(endPoint.getServiceName());
        PoolingHttpClientConnectionManager connectionManager = poolingConnectionManager(providerConfig);
        StandardHttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler(3, true);
        HttpClientBuilder httpClientBuilder = create().setConnectionManager(connectionManager).setRetryHandler(retryHandler);
        if (endPoint.getBaseUrl().getProtocol().equals("https")) {
            SSLContextBuilder sslContextBuilder = custom();
            try {
                if (endPoint.clientKeystoreExists()) {
                    sslContextBuilder.loadKeyMaterial(clientKeystoreBuilder.build(endPoint), endPoint.clientKeystorePassword().toCharArray());
                }
                if (endPoint.trustStoreExists()) {
                    sslContextBuilder.loadTrustMaterial(truststoreBuilder.build(endPoint), new TrustSelfSignedStrategy());
                }
                return create().setSSLContext(sslContextBuilder.build())
                        .setMaxConnPerRoute(providerConfig.getConnectionPoolMaxPerRoute())
                        .setMaxConnTotal(providerConfig.getConnectionPoolMaxTotal())
                        .setRetryHandler(retryHandler)
                        .build();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return httpClientBuilder.build();
    }

    private PoolingHttpClientConnectionManager poolingConnectionManager(ProviderConfig providerConfig) {
        PoolingHttpClientConnectionManager poolingClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingClientConnectionManager.setDefaultMaxPerRoute(providerConfig.getConnectionPoolMaxPerRoute());
        poolingClientConnectionManager.setMaxTotal(providerConfig.getConnectionPoolMaxTotal());
        return poolingClientConnectionManager;
    }
}

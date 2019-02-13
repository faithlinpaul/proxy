package in.co.ee.proxy.httpclient;

import java.security.KeyStore;

import org.springframework.stereotype.Component;

@Component
public class TruststoreBuilder {

    public KeyStore build(ProxiedHostConfiguration proxiedEndpoint) throws Exception {
        return new KeystoreBuilder(proxiedEndpoint.truststoreClasspathResource(), proxiedEndpoint.truststorePassword(),
                proxiedEndpoint.getTruststoreType()).build();
    }
}

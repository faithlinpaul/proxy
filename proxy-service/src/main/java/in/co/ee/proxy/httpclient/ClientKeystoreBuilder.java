package in.co.ee.proxy.httpclient;

import java.security.KeyStore;

import org.springframework.stereotype.Component;

@Component
public class ClientKeystoreBuilder {

    public KeyStore build(ProxiedHostConfiguration proxiedEndpoint)  {
        return new KeystoreBuilder(proxiedEndpoint.clientKeystoreClasspathResource(), proxiedEndpoint.clientKeystorePassword(),
                proxiedEndpoint.getKeystoreType()).build();
    }

}

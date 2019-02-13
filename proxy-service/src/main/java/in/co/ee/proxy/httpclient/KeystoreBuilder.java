package in.co.ee.proxy.httpclient;

import org.springframework.core.io.ClassPathResource;

import java.security.KeyStore;

public class KeystoreBuilder {

    private final ClassPathResource classpathResource;
    private final String password;
    private final String keystoreType;

    public KeystoreBuilder(ClassPathResource classpathResource, String password, String keystoreType) {
        this.classpathResource = classpathResource;
        this.password = password;
        this.keystoreType = keystoreType;
    }

    private KeyStore build(ClassPathResource clientKeystoreLocation, String keystorePassword) {
        if (!clientKeystoreLocation.exists()) {
            return null;
        }
        try {
            KeyStore clientKeyStore = KeyStore.getInstance(keystoreType);
            clientKeyStore.load(clientKeystoreLocation.getInputStream(), keystorePassword != null ? keystorePassword.toCharArray() : null);
            return clientKeyStore;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public KeyStore build() {
        return build(classpathResource, password);
    }

}

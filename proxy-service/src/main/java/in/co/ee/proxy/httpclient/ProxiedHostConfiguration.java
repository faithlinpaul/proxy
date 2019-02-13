package in.co.ee.proxy.httpclient;

import in.co.ee.proxy.config.ProviderType;
import lombok.Getter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.core.io.ClassPathResource;
import in.co.ee.proxy.config.ProviderConfig;

import java.net.MalformedURLException;
import java.net.URL;

@Getter
public class ProxiedHostConfiguration {
    private final ProviderType providerType;
    private URL baseUrl;
    private ClassPathResource keystoreClasspathLocation;
    private String clientKeystorePassword;
    private int requestTimeoutInMillis;
    private int connectTimeoutInMillis;
    private ClassPathResource truststoreClasspathLocation;
    private String truststorePassword;
    private String serviceName;

    public ProxiedHostConfiguration(String serviceName, ProviderConfig providerConfig) {
        this.serviceName = serviceName;
        try {
            String proxiedServiceUrl = providerConfig.getUrl();
            this.baseUrl = new ProxiedURL(proxiedServiceUrl).url();

            requestTimeoutInMillis = providerConfig.getRequestTimeoutMs();
            connectTimeoutInMillis = providerConfig.getConnectTimeoutMs();

            this.keystoreClasspathLocation = new ClassPathResource(keystoreLocation(serviceName,
                    providerConfig.getKeystoreBaseName()));

            this.truststoreClasspathLocation = new ClassPathResource(keystoreLocation(serviceName,
                    providerConfig.getTruststoreBaseName()));

            this.clientKeystorePassword = providerConfig.getKeystorePassword();
            this.truststorePassword = providerConfig.getTruststorePassword();
            this.providerType = providerConfig.getProviderType();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Please provide a valid base URL for proxied host", e);
        }
    }

    private String keystoreLocation(String serviceName, String clientKeystoreBasename) {
        if (serviceName != null) {
            return serviceName + "_" + clientKeystoreBasename;
        }
        return clientKeystoreBasename;
    }

    public ClassPathResource clientKeystoreClasspathResource() {
        return keystoreClasspathLocation;
    }

    public String clientKeystorePassword() {
        return clientKeystorePassword;
    }

    public ClassPathResource truststoreClasspathResource() {
        return truststoreClasspathLocation;
    }

    public boolean clientKeystoreExists() {
        return clientKeystoreClasspathResource().exists();
    }

    public boolean trustStoreExists() {
        return truststoreClasspathResource().exists();
    }

    public String truststorePassword() {
        return truststorePassword;
    }

    public String getKeystoreType() {
        return storeType(keystoreClasspathLocation);
    }

    private String storeType(ClassPathResource clientKeystoreClasspathResource) {
        if (clientKeystoreClasspathResource.getPath().endsWith("p12")) {
            return "PKCS12";
        }
        return "JKS";
    }

    public String getTruststoreType() {
        return storeType(truststoreClasspathLocation);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }


    public ProviderType getProviderType() {
        return providerType;
    }
}

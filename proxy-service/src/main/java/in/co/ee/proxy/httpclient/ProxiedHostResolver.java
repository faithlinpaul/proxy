package in.co.ee.proxy.httpclient;

import in.co.ee.proxy.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import in.co.ee.proxy.config.ProvidersConfig;
import in.co.ee.proxy.exception.ConfigurationException;

import javax.servlet.http.HttpServletRequest;

@Component
public class ProxiedHostResolver {

    private final ProvidersConfig configuration;

    @Autowired
    public ProxiedHostResolver(ProvidersConfig configuration) {
        this.configuration = configuration;
    }

    public ProxiedHostConfiguration resolve(HttpServletRequest request) {
        ProxiedHostConfiguration proxiedEndpoint = configuration.getEndpointConfiguration(Util.potentialProxiedServiceName(request));
        if (proxiedEndpoint == null) {
            throw new ConfigurationException(String.format(
                    "No Endpoint configured for client '%s'. Please provide one via proxy-service configuration.",
                    Util.potentialProxiedServiceName(request)));
        }
        return proxiedEndpoint;
    }
}

package in.co.ee.proxy.cache;

import in.co.ee.proxy.Util;
import in.co.ee.proxy.config.ProviderType;
import in.co.ee.proxy.config.ProvidersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CacheGroupKeyGenerator {

    @Autowired private SOAPBodyKeyGenerator soapBodyKeyGenerator;
    @Autowired private ProvidersConfig providersConfig;

    public String generateGroup(HttpServletRequest request) {
        String serviceName = Util.potentialProxiedServiceName(request);
        return String.format("%s_%s", serviceName, request.getHeader(CacheRequestHeader.CACHE_GROUP.toString()));
    }

    public String generateKey(HttpServletRequest request, HttpEntity<String> httpEntity) {
        if (providersConfig.getProviderType(Util.potentialProxiedServiceName(request)).equals(ProviderType.SOAP)) {
            return soapBodyKeyGenerator.generateKey(httpEntity.getBody());
        }
        return Util.potentialEndpointUrl(request);
    }
}

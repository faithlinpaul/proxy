package in.co.ee.proxy.service;

import in.co.ee.proxy.Util;
import in.co.ee.proxy.cache.*;
import in.co.ee.proxy.config.ProviderType;
import in.co.ee.proxy.config.ProvidersConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.OK;

@Service
public class ProxyService {

    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ProvidersConfig providersConfig;
    @Autowired
    private CacheGroupKeyGenerator cacheGroupKeyGenerator;

    @CacheInvalidatable
    public ResponseEntity<byte[]> get(HttpServletRequest request, HttpEntity<String> httpEntity) throws Exception {
        return getCachedResponseIfExist(request, httpEntity);
    }

    @CacheInvalidatable
    public ResponseEntity<byte[]> post(HttpServletRequest request, HttpEntity<String> httpEntity) throws Exception {
        if (providersConfig.getProviderType(Util.potentialProxiedServiceName(request)).equals(ProviderType.SOAP)) {
            return getCachedResponseIfExist(request, httpEntity);
        }

        return httpClientService.process(httpEntity, request);
    }

    @CacheInvalidatable
    public ResponseEntity<byte[]> put(HttpServletRequest request, HttpEntity<String> httpEntity) throws Exception {
        return httpClientService.process(httpEntity, request);
    }


    private ResponseEntity<byte[]> getCachedResponseIfExist(HttpServletRequest request, HttpEntity<String> httpEntity) throws Exception {
        String cacheHeader = request.getHeader(CacheRequestHeader.CACHE_GROUP.toString());
        String expireCacheHeader = request.getHeader(CacheRequestHeader.EXPIRE_CACHE_GROUP.toString());

        if (!StringUtils.isEmpty(expireCacheHeader) || StringUtils.isEmpty(cacheHeader)) {
            return httpClientService.process(httpEntity, request);
        }

        String group = cacheGroupKeyGenerator.generateGroup(request);
        String key = cacheGroupKeyGenerator.generateKey(request, httpEntity);
        CacheObject cacheObject = cacheService.get(group, key);
        if (cacheObject.isPresent()) {
            MDC.put("cached", "true");
            return cacheObject.toHttpResponse();
        }
        ResponseEntity<byte[]> responseEntity = httpClientService.process(httpEntity, request);
        if(responseEntity.getStatusCode().equals(OK)) {
            cacheObject = new CacheObject(responseEntity.getHeaders(), responseEntity.getBody());
            int ttl = providersConfig.getProviderConfig(Util.potentialProxiedServiceName(request)).getCacheDurationSecs();
            cacheService.set(group, key, cacheObject, ttl);
        }
        return responseEntity;
    }

}

package in.co.ee.proxy

import org.slf4j.MDC
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import in.co.ee.proxy.cache.CacheGroupKeyGenerator
import in.co.ee.proxy.cache.CacheObject
import in.co.ee.proxy.cache.CacheService
import in.co.ee.proxy.config.ProviderConfig
import in.co.ee.proxy.config.ProvidersConfig
import in.co.ee.proxy.service.HttpClientService
import in.co.ee.proxy.service.ProxyService

import javax.servlet.http.HttpServletRequest

import static in.co.ee.proxy.cache.CacheRequestHeader.CACHE_GROUP
import static in.co.ee.proxy.cache.CacheRequestHeader.EXPIRE_CACHE_GROUP


class ProxyServiceTest extends Specification {
    ProxyService service
    def clientService
    def cacheService
    def cacheGroupKeyGenerator
    def providersConfig

    def setup() {
        service = new ProxyService()
        clientService = Mock(HttpClientService)
        cacheService = Mock(CacheService)
        cacheGroupKeyGenerator = Mock(CacheGroupKeyGenerator)
        providersConfig = Mock(ProvidersConfig)
        service.httpClientService = clientService
        service.cacheService = cacheService
        service.cacheGroupKeyGenerator = cacheGroupKeyGenerator
        service.providersConfig = providersConfig
    }


    def "should not cache and read response from cache if cache header is not provided"() {
        given:
        HttpEntity<String> httpEntity
        def request = Mock(HttpServletRequest)
        def responseEntity = new ResponseEntity<byte[]>(HttpStatus.OK)

        when:
        service.get(request, httpEntity)

        then:
        1 * request.getHeader(CACHE_GROUP.toString()) >> null
        1 * clientService.process(null, request) >> responseEntity
        0 * cacheService.get(any(), any())
        0 * cacheService.set(any(), any(), any(), any())
        MDC.get("cached") == null
    }

    def "should cache response if cache header provided and response is not in the cache"() {
        given:
        HttpEntity<String> httpEntity
        def group = 'userservicegroup'
        def key = 'userservicekey'
        def request = Mock(HttpServletRequest)
        def responseEntity = new ResponseEntity<byte[]>(HttpStatus.OK)

        when:
        service.get(request, httpEntity)

        then:
        1 * request.getHeader(CACHE_GROUP.toString()) >> 'userservice'
        1 * request.getHeader(EXPIRE_CACHE_GROUP.toString()) >> ''
        1 * request.getContextPath() >> '/'
        1 * request.getRequestURI() >> '/alias/uri/path'
        1 * providersConfig.getProviderConfig('alias') >> new ProviderConfig(cacheDurationSecs: 10)
        1 * cacheGroupKeyGenerator.generateGroup(request) >> group
        1 * cacheGroupKeyGenerator.generateKey(request, httpEntity) >> key
        1 * clientService.process(null, request) >> responseEntity
        1 * cacheService.get(group, key) >> new CacheObject()
        1 * cacheService.set(group, key, _, 10)
    }

    def "should get cached response if cache header provided and response is present in the cache"() {
        given:
        def expectedBody = "Any String".getBytes()
        def group = 'userservicegroup'
        def key = 'userservicekey'
        HttpEntity<String> httpEntity = new HttpEntity<>()
        def request = Mock(HttpServletRequest)
        def responseEntity = new ResponseEntity<byte[]>('foo'.getBytes(), HttpStatus.OK)

        when:
        def actual = service.get(request, httpEntity)

        then:
        actual.getBody() == expectedBody
        1 * request.getHeader(CACHE_GROUP.toString()) >> 'userservice'
        1 * cacheGroupKeyGenerator.generateGroup(request) >> group
        1 * cacheGroupKeyGenerator.generateKey(request, httpEntity) >> key
        1 * cacheService.get(group, key) >> new CacheObject(httpBody: expectedBody)
        0 * clientService.process(null, request)
        0 * cacheService.set(request, responseEntity, httpEntity);
        MDC.get("cached") == "true"
    }

}

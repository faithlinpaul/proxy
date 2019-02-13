package in.co.ee.proxy.functionalTests

import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import groovyx.net.http.RESTClient
import org.junit.ClassRule
import org.junit.Rule
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ContextConfiguration
import redis.embedded.RedisServer
import spock.lang.Shared
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [in.co.ee.proxy.Application.class])
@WebIntegrationTest
class IntegrationBaseSpec extends Specification {


    RESTClient http

    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(wireMockConfig().port(1961)
                        .notifier(new ConsoleNotifier(true)))
    @Shared
    RedisServer redisServer = new RedisServer(6379);

    @ClassRule
    public static WireMockClassRule wireMockHttpsRule = new WireMockClassRule(wireMockConfig()
                    .httpsPort(1962)
                    .needClientAuth(true)
                    .keystorePath(new ClassPathResource("keystore.jks").getFile().getAbsolutePath())
                    .keystorePassword("password")
                    .trustStorePath(new ClassPathResource("cacerts.jks").getFile().getAbsolutePath())
                    .trustStorePassword("password")
                    .notifier(new ConsoleNotifier(true)))

    @Rule
    public WireMockClassRule instanceRule = wireMockRule;
    @Rule
    public WireMockClassRule instanceHttpsRule = wireMockHttpsRule;

    def setupSpec() {
        redisServer.start()
    }

    def cleanupSpec() {
        redisServer.stop()
    }

    def setup() {
        http = new RESTClient("http://localhost:1960/")
        http.handler.failure = http.handler.success

    }

    String transactionId() {
        '1234'
    }
}
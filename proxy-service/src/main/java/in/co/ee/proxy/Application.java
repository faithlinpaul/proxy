package in.co.ee.proxy;

import in.co.ee.proxy.config.DefaultConfig;
import in.co.ee.proxy.config.ProvidersConfig;
import in.co.ee.proxy.httpclient.ClientKeystoreBuilder;
import in.co.ee.proxy.httpclient.HttpClientFactory;
import in.co.ee.proxy.httpclient.ProxiedHostResolver;
import in.co.ee.proxy.httpclient.TruststoreBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.xml.transform.TransformerFactory;

/*
By default dev profile is been selected, simply run ./gradlew bootRun
available profiles are dev, qa, perf, ref
if want to run micro service other than dev env. use jar command with property file configuration
(e.g. java -jar proxy-service.jar --spring.config.location=file:/etc/opt/application-perf.yml)
*/

@Configuration
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@ComponentScan("in.co.ee")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    public static class ApplicationConfig  {

        @Autowired
        private ProvidersConfig providersConfig;
        @Autowired
        private DefaultConfig defaultConfig;


        @Bean
        public JedisPool jedisPool() {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(providersConfig.getCache().getThreadPoolSize());
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            return new JedisPool(poolConfig, providersConfig.getCache().getHost(), providersConfig.getCache().getPort(),
                    providersConfig.getCache().getTimeout());
        }

        @Bean
        public ProxiedHostResolver proxiedHostResolver() {
            return new ProxiedHostResolver(providersConfig);
        }

        @Bean
        public ClientKeystoreBuilder clientKeystoreBuilder() {
            return new ClientKeystoreBuilder();
        }

        @Bean
        public TruststoreBuilder truststoreBuilder() {
            return new TruststoreBuilder();
        }

        @Bean
        public HttpClientFactory httpClientFactory()  {
            return new HttpClientFactory(clientKeystoreBuilder(), truststoreBuilder(), providersConfig);
        }

        @Bean
        public TransformerFactory transformerFactory() {
            return javax.xml.transform.TransformerFactory.newInstance();
        }


    }
}
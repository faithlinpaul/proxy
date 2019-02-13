package in.co.ee.proxy.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.http.HttpEntity;

import java.net.URI;

public class GetMethodBuilder {

    protected HttpGet httpMethod;

    public GetMethodBuilder(URI url, HttpEntity<String> httpEntity, ProxiedHostConfiguration proxiedHostConfiguration) {
        httpMethod = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setConnectionRequestTimeout(proxiedHostConfiguration.getRequestTimeoutInMillis())
                .setConnectTimeout(proxiedHostConfiguration.getConnectTimeoutInMillis())
                .setSocketTimeout(proxiedHostConfiguration.getRequestTimeoutInMillis())
                .build();
        httpMethod.setConfig(requestConfig);
        new HttpHeaderCopier().copy(httpEntity.getHeaders(), httpMethod);
    }

    public HttpRequestBase method() {
        return httpMethod;
    }

}

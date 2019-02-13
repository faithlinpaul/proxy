package in.co.ee.proxy.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.URI;

public class PostMethodBuilder {

    protected HttpPost httpMethod;

    public PostMethodBuilder(URI proxiedEndpoint, HttpEntity<String> httpEntity, ProxiedHostConfiguration proxiedHostConfig)
            throws UnsupportedEncodingException {
        httpMethod = new HttpPost(proxiedEndpoint);
        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setConnectionRequestTimeout(proxiedHostConfig.getRequestTimeoutInMillis())
                .setConnectTimeout(proxiedHostConfig.getConnectTimeoutInMillis())
                .setSocketTimeout(proxiedHostConfig.getRequestTimeoutInMillis())
                .build();
        httpMethod.setConfig(requestConfig);

        new HttpHeaderCopier().copy(httpEntity.getHeaders(), httpMethod);

        MediaType contentType = httpEntity.getHeaders().getContentType();
        httpMethod.setEntity(new StringEntity(httpEntity.getBody(), ContentType.parse(contentType.toString())));
    }

    public HttpRequestBase method() {
        return httpMethod;
    }

}

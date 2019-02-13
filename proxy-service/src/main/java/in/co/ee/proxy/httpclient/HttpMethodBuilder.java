package in.co.ee.proxy.httpclient;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import in.co.ee.proxy.exception.ConfigurationException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.*;

@Component
public class HttpMethodBuilder {

    public HttpRequestBase build(HttpEntity<String> httpEntity, HttpServletRequest request, ProxiedHostConfiguration proxiedHostConfig)
            throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String url = proxiedHostConfig.getBaseUrl().toExternalForm()
                + RequestedPath.servletPathWithoutProxidedServiceName(request, proxiedHostConfig.getServiceName());
        url += getQueryParam(request);
        String decodedUrl = URLDecoder.decode(url, "UTF-8");
        URL proxiedUrl = new URL(decodedUrl);
        URI proxiedUri = new URI(proxiedUrl.getProtocol(), null, proxiedUrl.getHost(), proxiedUrl.getPort(),
                            proxiedUrl.getPath(), proxiedUrl.getQuery(), null);

        switch (requestMethod) {
            case GET:
                return new GetMethodBuilder(proxiedUri, httpEntity, proxiedHostConfig).method();
            case POST:
                return new PostMethodBuilder(proxiedUri, httpEntity, proxiedHostConfig).method();
            case PUT:
                return new PutMethodBuilder(proxiedUri, httpEntity, proxiedHostConfig).method();
            default:
                throw new ConfigurationException("Unsupported request method, only GET, POST, PUT, DELETE, HEAD and OPTIONS are allowed");
        }
    }

    private String getQueryParam(HttpServletRequest request) {
        return request.getQueryString() != null ? "?" + request.getQueryString() : StringUtils.EMPTY;
    }

}
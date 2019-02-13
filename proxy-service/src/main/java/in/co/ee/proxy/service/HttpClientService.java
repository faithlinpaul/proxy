package in.co.ee.proxy.service;

import in.co.ee.proxy.httpclient.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.valueOf;

@Component
public class HttpClientService {
    @Autowired
    private HttpClientFactory httpClientFactory;
    @Autowired
    private ProxiedHostResolver proxiedHostResolver;
    @Autowired
    private HttpMethodBuilder httpMethodBuilder;

    private final Map<ProxiedHostConfiguration, CloseableHttpClient> httpClients =
            synchronizedMap(new HashMap<ProxiedHostConfiguration, CloseableHttpClient>());

    private static Logger logger = getLogger(HttpClientService.class);

    public ResponseEntity<byte[]> process(HttpEntity<String> httpEntity, HttpServletRequest request) throws Exception {
        ProxiedHostConfiguration proxiedHostConfiguration = proxiedHostResolver.resolve(request);
        CloseableHttpClient httpClient = httpClients.computeIfAbsent(proxiedHostConfiguration, k -> httpClientFactory.newInstance(k));
        HttpRequestBase method = httpMethodBuilder.build(httpEntity, request, proxiedHostConfiguration);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(method);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(
                    toByteArray(response.getEntity().getContent()), new HttpHeadersWrapper(response.getAllHeaders()),
                    valueOf(response.getStatusLine().getStatusCode()));
            EntityUtils.consume(response.getEntity());
            return responseEntity;
        } catch (HttpHostConnectException e) {
            logger.error("Provider Connection Error:", e);
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        } finally {
            if(response != null) {
                response.close();
            }
            method.releaseConnection();
        }
    }
}

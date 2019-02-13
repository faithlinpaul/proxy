package in.co.ee.proxy.httpclient;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.HttpHeaders;

import java.util.List;

public class HttpHeaderCopier {

    protected void copy(HttpHeaders headers, HttpUriRequest httpMethod) {
        for (String headerName : headers.keySet()) {
            List<String> headerValues = headers.get(headerName);
            for (String headerValue : headerValues) {
                if(!headerName.equalsIgnoreCase("content-length") && !headerName.equalsIgnoreCase("host")) {
                    httpMethod.addHeader(headerName, headerValue);
                }
            }
        }
    }

}

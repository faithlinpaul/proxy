package in.co.ee.proxy.cache;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class CacheObject {
    private HttpHeaders httpHeaders;
    private byte[] httpBody;

    public CacheObject() {}

    public CacheObject(HttpHeaders httpHeaders, byte[] httpBody) {
        this.httpHeaders = httpHeaders;
        this.httpBody = httpBody;
    }

    public CacheObject(String header, byte[] body) {
        if (header != null) {
            httpHeaders = new HttpHeadersGsonSerializer().deserialize(header);
            httpHeaders.add(CacheResponseHeader.CACHED_RESPONSE.toString(), Boolean.TRUE.toString());
        }
        httpBody = body;
    }


    public String getHeadersAsAString() {
        return new HttpHeadersGsonSerializer().serialize(httpHeaders);
    }

    public byte[] getBodyAsAByteArray() {
        return httpBody;
    }

    public boolean isPresent() {
        return this.httpBody != null || this.httpHeaders != null;
    }

    public ResponseEntity<byte[]> toHttpResponse() {
        return new ResponseEntity<byte[]>(httpBody, httpHeaders, HttpStatus.OK);
    }
}

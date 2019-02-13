package in.co.ee.proxy.cache;

public enum CacheResponseHeader {

    CACHED_RESPONSE("Cached-Response");
    
    private String headerName;
    
    private CacheResponseHeader(String headerName) {
        this.headerName = headerName;
    }
    
    public String toString() {
        return headerName;
    }
}
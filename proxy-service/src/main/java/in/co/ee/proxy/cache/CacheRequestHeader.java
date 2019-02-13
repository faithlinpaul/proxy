package in.co.ee.proxy.cache;

public enum CacheRequestHeader {
    
    CACHE_GROUP("Cache-Group"),
    EXPIRE_CACHE_GROUP("Expire-Cache-Group");

    private String headerName;
        
    private CacheRequestHeader(String headerName) {
        this.headerName = headerName;
    }
    
    public String toString() {
        return headerName;
    }
}

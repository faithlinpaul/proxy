package in.co.ee.proxy.cache;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpHeaders;

import java.lang.reflect.Type;

public class HttpHeadersGsonSerializer {

    private Type serializableType;
    private Gson serializer;

    HttpHeadersGsonSerializer() {
        this.serializableType = new TypeToken<HttpHeaders>() {
        }.getType();
        this.serializer = new Gson();
    }

    HttpHeaders deserialize(String httpHeadersAsString) {
       return serializer.fromJson(httpHeadersAsString, this.serializableType);
    }

    String serialize(HttpHeaders headers) {
        return this.serializer.toJson(headers, serializableType);
    }
}
package in.co.ee.proxy.cache;

import in.co.ee.proxy.config.ProvidersConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class CacheService {
    public static final String BODY = "_body";
    public static final String HEADER = "_header";

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private CacheGroupKeyGenerator cacheGroupKeyGenerator;
    @Autowired
    private ProvidersConfig providersConfig;


    @Async
    public void set(String group, String key, CacheObject cacheObject, int ttl) {
        try ( Jedis jedis = jedisPool.getResource()) {
            Transaction t = jedis.multi();
            t.hset(group.getBytes(), (key + BODY).getBytes(), cacheObject.getBodyAsAByteArray());
            t.hset(group, (key + HEADER), cacheObject.getHeadersAsAString());
            t.expire(group.getBytes(), ttl);
            t.exec();
        }
    }


    public CacheObject get(String group, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] body = jedis.hget(group.getBytes(), (key + BODY).getBytes());
            String header = jedis.hget(group, (key + HEADER));
            return new CacheObject(header, body);
        } catch (Exception ex){
            getLogger(this.getClass()).info("Error retrieving from cache : " + ex);
            return new CacheObject();
        }

    }


}

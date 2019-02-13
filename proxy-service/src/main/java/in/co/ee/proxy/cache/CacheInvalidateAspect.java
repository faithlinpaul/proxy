package in.co.ee.proxy.cache;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
public class CacheInvalidateAspect {

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private CacheGroupKeyGenerator cacheGroupKeyGenerator;


    @Pointcut("@annotation(in.co.ee.proxy.cache.CacheInvalidatable)")
    public void invalidateCachePointCut() {}

    @Before("invalidateCachePointCut()")
    public void invalidateCache(JoinPoint joinPoint) {
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        String header = request.getHeader(CacheRequestHeader.EXPIRE_CACHE_GROUP.toString());
        if (!StringUtils.isEmpty(header)) {
            try(Jedis jedis = jedisPool.getResource()) {
                jedis.del(cacheGroupKeyGenerator.generateGroup(request));
            }
        }
    }
}

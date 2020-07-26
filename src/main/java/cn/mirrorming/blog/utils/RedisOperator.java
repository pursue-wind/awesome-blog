package cn.mirrorming.blog.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 @author mireal
 **/
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisOperator {
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Redis Set key_value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 1200, TimeUnit.SECONDS);
    }

    /**
     * Redis Set key_value with Timeout
     *
     * @param key
     * @param value
     * @param seconds
     */
    public void set(String key, String value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * Redis Get key
     *
     * @param key
     * @return value
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 判断 key 是否存在
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获得前缀为 xxx 的 Redis 的 Key
     *
     * @param prefix
     * @return
     */
    public Set<String> getPrefixKey(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "**");
        return keys;
    }

    /**
     * 删除key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 根据前缀删除key
     */
    public void deleteByPrex(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "**");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
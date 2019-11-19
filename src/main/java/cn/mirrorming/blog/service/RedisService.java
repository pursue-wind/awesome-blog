package cn.mirrorming.blog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author: mireal
 * @create: 2019-02-06 11:07
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisService {
    private final RedisTemplate redisTemplate;

    /**
     * @Param: [key, value, seconds 超时时间]
     * @Author: mirrorming
     */
    public void set(Object key, Object value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 根据前缀删除key
     */
    public void deleteByPrex(String prex) {
        prex = prex + "**";
        Set<String> keys = redisTemplate.keys(prex);
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
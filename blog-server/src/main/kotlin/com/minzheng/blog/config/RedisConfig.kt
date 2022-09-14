package com.minzheng.blog.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

/**
 * redis配置
 *
 * @author c
 * @date 2021/07/28
 */
@Configuration
class RedisConfig {
    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
        var redisTemplate: RedisTemplate<String, Any> = RedisTemplate()
        redisTemplate.setConnectionFactory(factory)
        val jackson2JsonRedisSerializer: Jackson2JsonRedisSerializer<*> = Jackson2JsonRedisSerializer(
            Any::class.java
        )
        var mapper = ObjectMapper()
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        jackson2JsonRedisSerializer.setObjectMapper(mapper)
        val stringRedisSerializer = StringRedisSerializer()
        // key采用String的序列化方式
        redisTemplate.keySerializer = stringRedisSerializer
        // hash的key也采用String的序列化方式
        redisTemplate.hashKeySerializer = stringRedisSerializer
        // value序列化方式采用jackson
        redisTemplate.valueSerializer = jackson2JsonRedisSerializer
        // hash的value序列化方式采用jackson
        redisTemplate.hashValueSerializer = jackson2JsonRedisSerializer
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }
}
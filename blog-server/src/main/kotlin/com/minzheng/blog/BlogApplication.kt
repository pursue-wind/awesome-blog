package com.minzheng.blog

import org.mybatis.spring.annotation.MapperScan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate

/**
 * 博客启动类
 *
 * @author c
 * @date 2021/08/14
 */
@MapperScan("com.minzheng.blog.dao")
@SpringBootApplication
@EnableScheduling
class BlogApplication {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}

fun main(args: Array<String>) {
    runApplication<BlogApplication>(*args)
}

inline fun <reified T> T.logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}

inline fun <R> notNull(vararg args: Any?, block: () -> R) =
    when {
        args.filterNotNull().size == args.size -> block()
        else -> null
    }
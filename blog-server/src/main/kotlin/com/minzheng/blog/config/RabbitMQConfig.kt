package com.minzheng.blog.config

import com.minzheng.blog.constant.MQPrefixConst
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Rabbitmq配置类
 *
 * @author c
 * @date 2021/07/29
 */
@Configuration
class RabbitMQConfig {
    @Bean
    fun articleQueue(): Queue {
        return Queue(MQPrefixConst.MAXWELL_QUEUE, true)
    }

    @Bean
    fun maxWellExchange(): FanoutExchange {
        return FanoutExchange(MQPrefixConst.MAXWELL_EXCHANGE, true, false)
    }

    @Bean
    fun bindingArticleDirect(): Binding {
        return BindingBuilder.bind(articleQueue()).to(maxWellExchange())
    }

    @Bean
    fun emailQueue(): Queue {
        return Queue(MQPrefixConst.EMAIL_QUEUE, true)
    }

    @Bean
    fun emailExchange(): FanoutExchange {
        return FanoutExchange(MQPrefixConst.EMAIL_EXCHANGE, true, false)
    }

    @Bean
    fun bindingEmailDirect(): Binding {
        return BindingBuilder.bind(emailQueue()).to(emailExchange())
    }
}
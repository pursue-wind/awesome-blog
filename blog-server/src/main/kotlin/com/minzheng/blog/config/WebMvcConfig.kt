package com.minzheng.blog.config

import com.minzheng.blog.handler.PageableHandlerInterceptor
import com.minzheng.blog.handler.WebSecurityHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * web mvc配置
 *
 * @author c
 * @date 2021/07/29
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Bean
    fun getWebSecurityHandler(): WebSecurityHandler {
        return WebSecurityHandler()
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedHeaders("*")
            .allowedOriginPatterns("*")
            .allowedMethods("*")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(PageableHandlerInterceptor())
        registry.addInterceptor(getWebSecurityHandler())
    }
}
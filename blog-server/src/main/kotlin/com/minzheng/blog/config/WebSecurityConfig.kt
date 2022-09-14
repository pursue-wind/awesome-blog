package com.minzheng.blog.config

import com.minzheng.blog.handler.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor
import org.springframework.security.web.session.HttpSessionEventPublisher

/**
 * Security配置类
 *
 * @author c
 * @date 2021/07/29
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val authenticationEntryPoint: AuthenticationEntryPointImpl,
    private val accessDeniedHandler: AccessDeniedHandlerImpl,
    private val authenticationSuccessHandler: AuthenticationSuccessHandler,
    private val authenticationFailHandler: AuthenticationFailHandlerImpl,
    private val logoutSuccessHandler: LogoutSuccessHandlerImpl,
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun securityMetadataSource(): FilterInvocationSecurityMetadataSource {
        return FilterInvocationSecurityMetadataSourceImpl()
    }

    @Bean
    fun accessDecisionManager(): AccessDecisionManager {
        return AccessDecisionManagerImpl()
    }

    @Bean
    fun sessionRegistry(): SessionRegistry {
        return SessionRegistryImpl()
    }

    @Bean
    fun httpSessionEventPublisher(): HttpSessionEventPublisher {
        return HttpSessionEventPublisher()
    }

    /**
     * 密码加密
     *
     * @return [PasswordEncoder] 加密方式
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * 配置权限
     *
     * @param http http
     * @throws Exception 异常
     */
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        // 配置登录注销路径
        http.formLogin()
            .loginProcessingUrl("/login")
            .successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailHandler)
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(logoutSuccessHandler)
        // 配置路由权限信息
        http.authorizeRequests()
            .withObjectPostProcessor(object : ObjectPostProcessor<FilterSecurityInterceptor> {
                override fun <O : FilterSecurityInterceptor> postProcess(fsi: O): O {
                    fsi!!.securityMetadataSource = securityMetadataSource()
                    fsi.accessDecisionManager = accessDecisionManager()
                    return fsi
                }
            })
            .anyRequest().permitAll()
            .and() // 关闭跨站请求防护
            .csrf().disable().exceptionHandling() // 未登录处理
            .authenticationEntryPoint(authenticationEntryPoint) // 权限不足处理
            .accessDeniedHandler(accessDeniedHandler)
            .and()
            .sessionManagement()
            .maximumSessions(20)
            .sessionRegistry(sessionRegistry())
    }
}
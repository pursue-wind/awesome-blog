package cn.mirrorming.blog.config;

import cn.mirrorming.blog.security.constant.SecurityConstants;
import cn.mirrorming.blog.security.properties.SecurityProperties;
import cn.mirrorming.blog.security.support.manager.SecurityConfigProvider;
import cn.mirrorming.blog.security.support.sms.SmsCodeAuthenticationFilter;
import cn.mirrorming.blog.security.support.validate.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.util.List;

/**
 * 认证相关的扩展点配置。配置在这里的 bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全模块默认的配置。
 *
 * @author Mireal
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthenticationServerConfig {

    /**
     * 配置表单登录
     */
    @Bean
    public SecurityConfigProvider passwordAuthenticationConfig(AuthenticationSuccessHandler myAuthenticationSuccessHandler,
                                                               AuthenticationFailureHandler myAuthenticationFailureHandler) {
        return http -> http.formLogin()
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler);
    }

    /**
     * 校验码相关安全配置
     */
    @Bean
    public SecurityConfigProvider validateCodeSecurityConfig(ValidateCodeFilter validateCodeFilter) {
        // 实现所有验证码过滤器第一层过滤器
        return http -> http.addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }

    /**
     * 配置验证码登录器
     */
    @Bean
    public SecurityConfigProvider smsCodeAuthenticationSecurityConfig(AuthenticationSuccessHandler myAuthenticationSuccessHandler,
                                                                      AuthenticationFailureHandler myAuthenticationFailureHandler,
                                                                      AuthenticationManager providerManager) {
        return http -> {
            SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
            smsCodeAuthenticationFilter.setAuthenticationManager(providerManager);
            smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
            smsCodeAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
            http.addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        };
    }


    /**
     * 默认密码处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        // bcrypt的加密强度4-31
        return new BCryptPasswordEncoder(5);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
                                                               @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager providerManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }

    @Bean
    public UserDetailsChecker accountStatusUserDetailsChecker() {
        return new AccountStatusUserDetailsChecker();
    }

    /**
     * Redis tokenStore
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("userAuth:");
        return redisTokenStore;
    }

    /**
     * Jwt转换器
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("SpringSecurityShuShiNiuPi");
        return converter;
    }

    /**
     * 使用 Jwt 储存 token
     *
     * @param jwtAccessTokenConverter
     * @return
     */
/*    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }*/
}

package cn.mirrorming.blog.config;


import cn.mirrorming.blog.security.support.manager.SecurityConfigProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @author Mireal
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private SpringSocialConfigurer mirealSocialSecurityConfig;
    @Autowired
    private AccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private SecurityConfigProviderManager securityConfigProviderManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        securityConfigProviderManager.config(http);
        http
                .apply(mirealSocialSecurityConfig)
                .and()
                .csrf()
                .requireCsrfProtectionMatcher(request -> !request.getServletPath().contains("/druid"))
                .disable();
        http.authorizeRequests().anyRequest().permitAll();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(myAccessDeniedHandler);
    }
}

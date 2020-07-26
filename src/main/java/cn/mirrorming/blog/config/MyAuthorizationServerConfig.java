package cn.mirrorming.blog.config;

import cn.mirrorming.blog.security.properties.SecurityProperties;
import cn.mirrorming.blog.security.properties.CommonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * 认证服务器配置
 *
 * @author Mireal
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(CommonProperties.class)
public class MyAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager providerManager;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .authenticationManager(providerManager)
                .userDetailsService(userDetailsService);
//                //jwt 暂时不用，需要直接打开即可
//                .accessTokenConverter(jwtAccessTokenConverter);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        securityProperties.getOauth2().getClients()
                .forEach(client -> builder.withClient(client.getClientId()).secret(client.getClientSecret())
                        .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                        .accessTokenValiditySeconds(client.getAccessTokenSeconds()).refreshTokenValiditySeconds(2592000)
                        .scopes("all"));
    }
}
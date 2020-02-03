package cn.mirrorming.blog.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * 认证服务器配置
 *
 * @author Mireal
 * @Date 2019/9/4 14:06
 * @since v1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * 注入用于支持 password 模式
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Bean
//    public TokenStore tokenStore() {
//        // 基于 JDBC 实现，令牌保存到数据
//        return new JdbcTokenStore(dataSource());
//    }

    @Bean
    RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public ClientDetailsService jdbcClientDetails() {
        // 基于 JDBC 实现，需要事先在数据库配置客户端信息
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许客户端访问 /oauth/check_token 检查 token
                //.checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 设置令牌
        endpoints
                // 用于支持密码模式
                .authenticationManager(authenticationManager)
//                .tokenStore(tokenStore());

                .tokenStore(redisTokenStore());
    }

//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        // 读取客户端配置
//        clients.withClientDetails(jdbcClientDetails());
//    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                // 使用内存设置
                .inMemory()
                // client_id
                .withClient("client")
                // client_secret
                .secret(passwordEncoder.encode("secret"))
                // 授权类型，密码模式和刷新令牌
                .authorizedGrantTypes("password", "refresh_token")
                // 授权范围
                .scopes("backend")
                // 可以设置对哪些资源有访问权限，不设置则全部资源都可以访问
                .resourceIds("backend-resources")
                // 设置访问令牌的有效期，这里是 1 天
                .accessTokenValiditySeconds(60 * 60 * 24)
                // 设置刷新令牌的有效期，这里是 30 天
                .refreshTokenValiditySeconds(60 * 60 * 24 * 30);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
    }
}
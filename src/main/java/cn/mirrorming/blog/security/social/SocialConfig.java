package cn.mirrorming.blog.security.social;

import cn.mirrorming.blog.config.properties.SecurityProperties;
import cn.mirrorming.blog.security.social.support.MirealSpringSocialConfigurer;
import cn.mirrorming.blog.security.social.support.SocialAuthenticationFilterPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * 社交登录配置主类
 *
 * @author Mireal
 * @version V1.0
 * @date 2019/12/1 17:25
 */
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Autowired(required = false)
    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;


    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // 第三个参数是对插入到数据库中的数据做加密，这里没有做任何处理，即noOpText
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        // 为表名增加前缀
        repository.setTablePrefix("mirrorming_");

        if (connectionSignUp != null) {
            repository.setConnectionSignUp(connectionSignUp);
        }
        return repository;
    }

    /**
     * 社交登录配置类，供浏览器或app模块引入设计登录配置用。
     *
     * @return
     */
    @Bean
    public SpringSocialConfigurer mirealSocialSecurityConfig() {
        // SocialAuthenticationFilter过滤器默认拦截的请求是/auth开头，这里获取自己配置的处理路径/login
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        MirealSpringSocialConfigurer configurer = new MirealSpringSocialConfigurer(filterProcessesUrl);
        configurer.signupUrl("/mireal-signUp.html");
        configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
        return configurer;
    }


    /**
     * 用来处理注册流程的工具类
     *
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }
}
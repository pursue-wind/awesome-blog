package cn.mirrorming.blog.security.social.gitee.config;

import cn.mirrorming.blog.config.properties.GiteeProperties;
import cn.mirrorming.blog.config.properties.SecurityProperties;
import cn.mirrorming.blog.security.social.gitee.connect.GiteeConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;

/**
 * @Description gitee 社交登录的自动配置
 * @Author Mireal
 * @Version 1.0
 */
@Configuration
@EnableSocial
@ConditionalOnProperty(prefix = "mireal.security.social.gitee", name = "app-id")
public class GiteeAutoAuthConfig extends SocialConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    public ConnectionFactory<?> createConnectionFactory() {
        GiteeProperties gitee = securityProperties.getSocial().getGitee();
        return new GiteeConnectionFactory(gitee.getProviderId(), gitee.getAppId(), gitee.getAppSecret());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }
}
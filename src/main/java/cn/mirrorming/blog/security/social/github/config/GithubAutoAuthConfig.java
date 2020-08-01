package cn.mirrorming.blog.security.social.github.config;

import cn.mirrorming.blog.security.social.properties.GithubProperties;
import cn.mirrorming.blog.security.properties.SecurityProperties;
import cn.mirrorming.blog.security.social.github.connect.GitHubConnectionFactory;
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
 * github 社交登录的自动配置
 *
 * @author Mireal Chan
 */
@Configuration
@EnableSocial
@ConditionalOnProperty(prefix = "security.social.github", name = "app-id")
public class GithubAutoAuthConfig extends SocialConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(createConnectionFactory());
    }

    public ConnectionFactory<?> createConnectionFactory() {
        GithubProperties github = securityProperties.getSocial().getGithub();
        return new GitHubConnectionFactory(github.getProviderId(), github.getAppId(), github.getAppSecret());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }
}
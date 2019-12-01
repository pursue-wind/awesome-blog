/**
 *
 */
package cn.mirrorming.blog.security.social.qq.config;

import cn.mirrorming.blog.config.properties.QQProperties;
import cn.mirrorming.blog.config.properties.SecurityProperties;
import cn.mirrorming.blog.security.social.miss.SocialAutoConfigurerAdapter;
import cn.mirrorming.blog.security.social.qq.connet.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * 只有在application.yml中配置了appId，这个类中的配置才会生效
 * @author mireal
 */
@Configuration
@ConditionalOnProperty(prefix = "mireal.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qqConfig = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
    }
}
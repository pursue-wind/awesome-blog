package cn.mirrorming.blog.security.social.miss;

import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactory;

/**
 * Social包在SpringBoot2.x移除了 所以直接复制代码过来
 *
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/1 18:08
 */
@NoArgsConstructor
public abstract class SocialAutoConfigurerAdapter extends SocialConfigurerAdapter {
    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment environment) {
        configurer.addConnectionFactory(this.createConnectionFactory());
    }

    protected abstract ConnectionFactory<?> createConnectionFactory();
}
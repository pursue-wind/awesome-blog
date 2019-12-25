package cn.mirrorming.blog.security.social.gitee.connect;

import cn.mirrorming.blog.security.social.gitee.api.Gitee;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @Author Mireal
 * @Date 2019-11-13 21:49
 * @Version 1.0
 */
public class GiteeConnectionFactory extends OAuth2ConnectionFactory<Gitee> {

    public GiteeConnectionFactory(String providerId, String clientId, String clientSecret) {
        super(providerId, new GiteeServiceProvider(clientId, clientSecret), new GiteeAdapter());
    }
}
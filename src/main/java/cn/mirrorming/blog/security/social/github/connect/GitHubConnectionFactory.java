package cn.mirrorming.blog.security.social.github.connect;

import cn.mirrorming.blog.security.social.github.api.GitHub;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @author Mireal Chan
 * @Date 2019-11-13 21:49
 * @Version 1.0
 */
public class GitHubConnectionFactory extends OAuth2ConnectionFactory<GitHub> {

    public GitHubConnectionFactory(String providerId, String clientId, String clientSecret) {
        super(providerId, new GitHubServiceProvider(clientId, clientSecret), new GitHubAdapter());
    }
}
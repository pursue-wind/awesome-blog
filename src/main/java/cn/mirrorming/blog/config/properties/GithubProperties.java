package cn.mirrorming.blog.config.properties;

import lombok.Data;
import cn.mirrorming.blog.security.social.miss.SocialProperties;

/**
 * @Description github第三方登录配置
 * @author Mireal
 * @Version 1.0
 */
@Data
public class GithubProperties extends SocialProperties {
    private String providerId = "github";
}

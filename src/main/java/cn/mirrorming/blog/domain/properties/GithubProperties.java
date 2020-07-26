package cn.mirrorming.blog.domain.properties;

import cn.mirrorming.blog.security.social.miss.SocialProperties;
import lombok.Data;

/**
 * github第三方登录配置
 * @author Mireal Chan
 * @Version 1.0
 */
@Data
public class GithubProperties extends SocialProperties {
    private String providerId = "github";
}

package cn.mirrorming.blog.config.properties;

import lombok.Data;
import cn.mirrorming.blog.security.social.miss.SocialProperties;

/**
 * @Description github第三方登录配置
 * @author Mireal
 * @Date 2019-11-13 21:49
 * @Version 1.0
 */
@Data
public class GiteeProperties extends SocialProperties {
    private String providerId = "gitee";
}

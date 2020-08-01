package cn.mirrorming.blog.security.social.properties;

import cn.mirrorming.blog.security.social.miss.SocialProperties;
import lombok.Data;

/**
 * github第三方登录配置
 * @author Mireal Chan
 * @Date 2019-11-13 21:49
 * @Version 1.0
 */
@Data
public class GiteeProperties extends SocialProperties {
    private String providerId = "gitee";
}

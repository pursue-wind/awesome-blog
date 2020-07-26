package cn.mirrorming.blog.domain.properties;

import cn.mirrorming.blog.security.social.miss.SocialProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/1 17:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQProperties extends SocialProperties {
    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 qq。
     */
    private String providerId = "qq";
}

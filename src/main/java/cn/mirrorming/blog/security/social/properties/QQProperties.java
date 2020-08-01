package cn.mirrorming.blog.security.social.properties;

import cn.mirrorming.blog.security.social.miss.SocialProperties;
import lombok.*;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/1 17:56
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QQProperties extends SocialProperties {
    /**
     * 第三方id，用来决定发起第三方登录的url，默认是 qq。
     */
    @Builder.Default
    private String providerId = "qq";
}

package cn.mirrorming.blog.security.social.miss;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Social包在SpringBoot2.x移除了 所以直接复制代码过来
 *
 * @author Mireal
 * @version V1.0
 * @date 2019/12/1 18:08
 */
@Data
@NoArgsConstructor
public abstract class SocialProperties {
    private String appId;
    private String appSecret;
}
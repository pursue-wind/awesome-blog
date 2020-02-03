/**
 *
 */
package cn.mirrorming.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mireal
 *
 */
@Data
@ConfigurationProperties(prefix = "mireal.security")
public class SecurityProperties {
    private MirealSocialProperties social = new MirealSocialProperties();

    /**
     * 验证码配置
     */
//    private ValidateCodeProperties code = new ValidateCodeProperties();
    /**
     * 社交登录配置
     */
//    private SocialProperties social = new SocialProperties();
    /**
     * OAuth2认证服务器配置
     */
//    private OAuth2Properties oauth2 = new OAuth2Properties();


}


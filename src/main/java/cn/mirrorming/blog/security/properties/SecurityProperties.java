package cn.mirrorming.blog.security.properties;

import cn.mirrorming.blog.security.social.properties.MirealSocialProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Mireal
 */
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private ValidateCodeProperties validateCode = new ValidateCodeProperties();
    private Oauth2Properties oauth2 = new Oauth2Properties();
    private MirealSocialProperties social = new MirealSocialProperties();
}

package cn.mirrorming.blog.security.properties;

import lombok.Data;

/**
 * 验证码配置
 *
 * @author Mireal
 */
@Data
public class ValidateCodeProperties {
    private SmsCodeProperties smsCode = new SmsCodeProperties();
    private ImageCodeProperties imageCode = new ImageCodeProperties();
}

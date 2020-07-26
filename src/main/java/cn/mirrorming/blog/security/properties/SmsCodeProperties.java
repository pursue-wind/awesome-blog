package cn.mirrorming.blog.security.properties;

import lombok.Data;

/**
 * 短信验证码配置
 *
 * @author Mireal
 */
@Data
public class SmsCodeProperties {

    private Integer length = 4;

    private Integer expireIn = 300;

    private String url;


}

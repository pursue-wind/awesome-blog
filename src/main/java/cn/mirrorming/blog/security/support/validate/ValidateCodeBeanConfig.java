package cn.mirrorming.blog.security.support.validate;

import cn.mirrorming.blog.security.properties.SecurityProperties;
import cn.mirrorming.blog.security.support.validate.sms.SmsCodeSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * 验证码相关的扩展点配置。配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全 模块默认的配置。
 *
 * @author Mireal
 */
@Slf4j
@Configuration
public class ValidateCodeBeanConfig {



    /**
     * 短信验证码生成器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "smsCodeGenerator")
    public ValidateCodeGenerator smsCodeGenerator(SecurityProperties securityProperties) {
        return request -> {
            String code = RandomStringUtils.randomNumeric(securityProperties.getValidateCode().getSmsCode().getLength());
            Integer expireIn = securityProperties.getValidateCode().getSmsCode().getExpireIn();
            return ValidateCode.of(code, expireIn);
        };
    }

    /**
     * 图片验证码生成器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(SecurityProperties securityProperties) {
        return request -> {
            int width = ServletRequestUtils.getIntParameter(request.getRequest(), "width", securityProperties.getValidateCode().getImageCode().getWidth());
            int height = ServletRequestUtils.getIntParameter(request.getRequest(), "height", securityProperties.getValidateCode().getImageCode().getHeight());
            int length = securityProperties.getValidateCode().getImageCode().getLength();
            int expireIn = securityProperties.getValidateCode().getImageCode().getExpireIn();
            return ImageCodeUtils.generate(width, height, length, expireIn);
        };
    }

    /**
     * 短信验证码发送器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return (String mobile, String smsScene, String code) -> {
            log.warn("请配置真实的短信验证码发送器(SmsCodeSender)");
            log.info("向手机" + mobile + "发送短信验证码" + code + "发送场景为" + smsScene);
        };
    }
}

package cn.mirrorming.blog.security.support.validate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码生成器接口
 *
 * @author Mireal
 */
@FunctionalInterface
public interface ValidateCodeGenerator {

    /**
     * 生成校验码
     */
    ValidateCode generate(ServletWebRequest request);
}

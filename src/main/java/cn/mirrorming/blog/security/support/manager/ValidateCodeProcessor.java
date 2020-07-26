package cn.mirrorming.blog.security.support.manager;

import cn.mirrorming.blog.security.support.validate.ValidateCodeException;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同校验码的处理逻辑
 *
 * @author Mireal
 */
public interface ValidateCodeProcessor {

    /**
     * 创建校验码
     *
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param servletWebRequest
     * @throws Exception
     */
    void validate(ServletWebRequest servletWebRequest) throws ValidateCodeException;
}

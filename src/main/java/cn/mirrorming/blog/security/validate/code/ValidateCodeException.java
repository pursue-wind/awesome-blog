package cn.mirrorming.blog.security.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/2 22:50
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}

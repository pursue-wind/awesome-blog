package cn.mirrorming.blog.security.handler;

import cn.mirrorming.blog.security.constant.StatusConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author Mireal
 */
@Slf4j
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ResponseExport responseExport;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String message = exception.getMessage();
        log.info("登录失败:" + message);
        if (StringUtils.equals(message, "坏的凭证") || StringUtils.equals(message, "Bad credentials")) {
            message = "用户不存在或密码错误";
        }
        responseExport.failure(response, HttpStatus.UNAUTHORIZED.value(), StatusConstants.BAD_CREDENTIAL, message);
    }
}


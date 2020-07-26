package cn.mirrorming.blog.security.handler;


import cn.mirrorming.blog.security.constant.StatusConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登陆返回 json
 *
 * @author Mireal
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ResponseExport responseExport;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        String message = StringUtils.split(authException.getMessage(), ":")[0];
        switch (message) {
            case ("Invalid access token"):
                responseExport.failure(response, HttpStatus.UNAUTHORIZED.value(), StatusConstants.CREDENTIAL_INVALID, "登陆已失效");
                break;
            case ("Access token expired"):
                responseExport.failure(response, HttpStatus.UNAUTHORIZED.value(), StatusConstants.CREDENTIAL_EXPIRED, "登陆已过期");
                break;
            case ("Full authentication is required to access this resource"):
                responseExport.failure(response, HttpStatus.UNAUTHORIZED.value(), StatusConstants.NOT_AUTHENTICATED, "未登录");
                break;
            default:
                responseExport.failure(response, HttpStatus.UNAUTHORIZED.value(), StatusConstants.BAD_CREDENTIAL, message);
        }
    }
}
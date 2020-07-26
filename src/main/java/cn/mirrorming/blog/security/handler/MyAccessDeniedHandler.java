package cn.mirrorming.blog.security.handler;

import cn.mirrorming.blog.security.constant.StatusConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义权限不足返回json
 *
 * @author Mireal
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ResponseExport responseExport;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException {
        responseExport.failure(response, HttpStatus.FORBIDDEN.value(), StatusConstants.NO_AUTHORITY, accessDeniedException.getMessage());
    }
}

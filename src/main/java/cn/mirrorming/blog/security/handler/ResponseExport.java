package cn.mirrorming.blog.security.handler;

import cn.mirrorming.blog.domain.common.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用于 response 统一处理
 *
 * @author Mireal
 */
@Component
public class ResponseExport {

    @Autowired
    private ObjectMapper objectMapper;
    @Value("${config.vue.address}")
    private String vueAddress;

    public void success(HttpServletResponse response, Object data) throws IOException {
        common(response, HttpStatus.OK.value(), R.succeed(data));
    }

    public void successAndRedirect(HttpServletResponse response, String token) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
//        response.setHeader("Authorization", token);
//        response.getWriter().write(objectMapper.writeValueAsString(token));
        response.sendRedirect(vueAddress + "/login?token=" + token);
    }

    public void failure(HttpServletResponse response, int httpStatus, int statusCode, String message) throws IOException {
        common(response, httpStatus, R.fail(statusCode, message));
    }

    private void common(HttpServletResponse response, int httpStatus, R responseVO) throws IOException {
        String json = objectMapper.writeValueAsString(responseVO);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        response.setStatus(httpStatus);
    }
}

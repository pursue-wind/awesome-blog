package cn.mirrorming.blog.security;


import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.utils.JacksonUtils;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Mireal Chan
 */
@UtilityClass
public class SecurityUtil {

    public void writeJavaScript(R r, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JacksonUtils.obj2json(r));
        printWriter.flush();
    }

    /**
     * 获取Authentication
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     **/
    public MyUserDetails getUser() {
        try {
            return (MyUserDetails) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new AppException("登录状态过期");
        }
    }

    /**
     * 获取用户
     **/
    public int getUserId() {
        try {
            MyUserDetails user = (MyUserDetails) getAuthentication().getPrincipal();
            return user.getId();
        } catch (Exception e) {
            throw new AppException("登录状态过期");
        }
    }
}
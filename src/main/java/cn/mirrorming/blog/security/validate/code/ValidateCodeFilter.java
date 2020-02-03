package cn.mirrorming.blog.security.validate.code;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/2 22:47
 */
public class ValidateCodeFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equals("/biaodan", request.getRequestURI())
                && StringUtils.equals(request.getMethod(), "post")) {
            validate(new ServletWebRequest(request));
        }
        filterChain.doFilter(request, response);
    }

    private void validate(ServletWebRequest servletWebRequest) {
    }
}

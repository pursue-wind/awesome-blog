package cn.mirrorming.blog.security.support.validate;

import cn.mirrorming.blog.security.constant.SecurityConstants;
import cn.mirrorming.blog.security.support.manager.ValidateCodeProcessorManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码过滤器,图片验证码,短信验证码通用
 *
 * @author Mireal
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private ValidateCodeProcessorManager validateCodeProcessorManager;

    @Autowired
    private ServerProperties serverProperties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private Map<String, ValidateCodeType> urlMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String contextPath = serverProperties.getServlet().getContextPath();
        contextPath = StringUtils.isEmpty(contextPath) ? "" : contextPath;
        urlMap.put(contextPath + SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_SMS, ValidateCodeType.sms);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ValidateCodeType type = getValidateCodeType(request);
        if (type != null) {
            try {
                ServletWebRequest webRequest = new ServletWebRequest(request, response);

                webRequest.setAttribute(SecurityConstants.DEFAULT_PARAMETER_NAME_SCENE, SecurityConstants.DEFAULT_VALIDATE_SCENE, RequestAttributes.SCOPE_REQUEST);

                validateCodeProcessorManager.findValidateCodeProcessor(type).validate(new ServletWebRequest(request, response));

            } catch (AuthenticationException exception) {
                myAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        for (Map.Entry<String, ValidateCodeType> entry : urlMap.entrySet()) {
            if (pathMatcher.match(entry.getKey(), request.getRequestURI())) {
                return entry.getValue();
            }
        }
        return null;
    }
}

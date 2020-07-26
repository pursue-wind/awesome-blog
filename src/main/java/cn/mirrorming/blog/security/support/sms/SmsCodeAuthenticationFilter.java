package cn.mirrorming.blog.security.support.sms;

import cn.mirrorming.blog.security.constant.SecurityConstants;
import cn.mirrorming.blog.security.constant.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 短信登录过滤器
 *
 * @author Mireal
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // ~ Static fields/initializers
    // =====================================================================================

    private String mobileParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_USERNAME;

    private String mobileRegex = CommonConstants.DEFAULT_MOBILE_REGEX;

    private boolean postOnly = true;

    // ~ Constructors
    // ===================================================================================================

    public SmsCodeAuthenticationFilter() {
        //默认的手机验证码登录请求处理urlpost请求
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_SMS, "POST"));
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)//
            throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //获取手机号
        String mobile = Optional.ofNullable(obtainMobile(request)).orElse("").trim();

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        //返回被成功认证的用户
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取手机号
     */
    protected String obtainMobile(HttpServletRequest request) {
        String mobile = StringUtils.trim(request.getParameter(mobileParameter));
        if (!Pattern.matches(mobileRegex, mobile)) {
            throw new AuthenticationServiceException("手机号码格式不正确");
        }
        return mobile;
    }

    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {//创建新的身份验证详细信息实例时
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    public void setMobileParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.mobileParameter = usernameParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }
}

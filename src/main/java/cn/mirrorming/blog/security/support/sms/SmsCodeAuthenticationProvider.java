package cn.mirrorming.blog.security.support.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 短信登录验证逻辑
 * <p>
 * 由于短信验证码的验证在过滤器里已完成，这里直接读取用户信息即可。
 */
@Component
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserDetailsChecker accountStatusUserDetailsChecker;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());

        if (userDetails == null) {
            throw new UsernameNotFoundException("无法获取用户信息");
        }

        accountStatusUserDetailsChecker.check(userDetails);

        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

}

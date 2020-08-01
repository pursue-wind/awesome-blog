package cn.mirrorming.blog.security.handler;

import cn.mirrorming.blog.security.constant.StatusConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * 认证成功处理器
 *
 * @author Mireal
 */
@Slf4j
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ResponseExport responseExport;

    @Autowired
    private BasicTokenCheck basicTokenCheck;

    @Autowired
    private AuthorizationServerTokenServices defaultTokenServices;

    @Autowired
    private TokenStore tokenStore;

    private static final boolean removeExistingToken = true;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        log.info("用户" + username + "登录成功");
        OAuth2AccessToken oauth2AccessToken;
        try {
            oauth2AccessToken = generateAccessToken(request, response, authentication);
            if (request.getRequestURI().contains("auth/")) {
                responseExport.successAndRedirect(response, oauth2AccessToken.getValue());
            } else {
                responseExport.success(response, oauth2AccessToken);
            }
        } catch (AuthenticationException e) {
            responseExport.failure(response, HttpStatus.UNAUTHORIZED.value(), StatusConstants.BAD_CREDENTIAL, e.getMessage());
        }
    }

    private OAuth2AccessToken generateAccessToken(HttpServletRequest request, HttpServletResponse response,
                                                  Authentication authentication) throws IOException {

        String header = request.getHeader("Authorization");

        ClientDetails clientDetails = basicTokenCheck.obtainClientDetails(header);

        TokenRequest tokenRequest = new TokenRequest(Collections.emptyMap(), clientDetails.getClientId(), clientDetails.getScope(), "custom");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        if (removeExistingToken) {
            OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(oAuth2Authentication);
            if (existingAccessToken != null) {
                tokenStore.removeAccessToken(existingAccessToken);
            }
        }
        return defaultTokenServices.createAccessToken(oAuth2Authentication);
    }

}

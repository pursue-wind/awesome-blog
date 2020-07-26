package cn.mirrorming.blog.security.handler;


import com.baomidou.mybatisplus.extension.api.R;
import com.google.common.collect.Lists;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Mireal
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private BasicTokenCheck basicTokenCheck;

    private TokenGranter tokenGranter;

    private static final String GRANT_TYPE = "refresh_token";

    private static final List<String> scopeList = Lists.newArrayList("all");

    public OAuth2Controller(BasicTokenCheck basicTokenCheck, ClientDetailsService clientDetailsService, AuthorizationServerTokenServices defaultAuthorizationServerTokenServices) {
        this.basicTokenCheck = basicTokenCheck;
        this.tokenGranter = new RefreshTokenGranter(defaultAuthorizationServerTokenServices, clientDetailsService, null);
    }

    @PostMapping("/token/refresh")
    public R refreshToken(@RequestHeader("Authorization") String header,
                          @RequestParam Map<String, String> parameters) {

        if (parameters.get(GRANT_TYPE) == null) {
            throw new IllegalArgumentException("refresh_token不能为空");
        }

        ClientDetails clientDetails = basicTokenCheck.obtainClientDetails(header);

        TokenRequest tokenRequest = new TokenRequest(parameters, clientDetails.getClientId(), scopeList, GRANT_TYPE);

        OAuth2AccessToken token = tokenGranter.grant(tokenRequest.getGrantType(), tokenRequest);

        return R.ok(token);
    }

}

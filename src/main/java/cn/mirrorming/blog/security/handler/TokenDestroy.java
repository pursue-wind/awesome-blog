package cn.mirrorming.blog.security.handler;

import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.mapper.generate.UsersMapper;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Mireal
 */
@Component
public class TokenDestroy {

    @Autowired
    private UsersMapper userRepository;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private TokenStore tokenStore;

    public void tokenDestroy(Long userId, String clientId) {

        ClientDetails clientDetails = null;

        clientDetails = clientDetailsService.loadClientByClientId(clientId);

        Users user = Optional.ofNullable(userRepository.selectById(userId)).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        TokenRequest tokenRequest = new TokenRequest(Maps.newHashMap(), clientId, clientDetails.getScope(), "custom");

        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword());

        OAuth2Authentication oauth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(oauth2Authentication);

        if (existingAccessToken != null) {
            tokenStore.removeAccessToken(existingAccessToken);
        }
    }
}

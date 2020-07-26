package cn.mirrorming.blog.security.handler;

import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * 基本令牌检查
 *
 * @author Mireal
 */
@Component
public class BasicTokenCheck {

    private ClientDetailsService clientDetailsService;

    public BasicTokenCheck(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    public final ClientDetails obtainClientDetails(String header) {
        header = "Basic YmxvZzpibG9n"; // 写死clientId 和 secret 为 blog
        if (header == null || !header.startsWith("Basic ")) {
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }

        byte[] base64Token = header.substring(6).getBytes(CharsetUtil.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, CharsetUtil.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }

        String clientId = token.substring(0, delim);
        String clientSecret = token.substring(delim + 1);

        ClientDetails clientDetails;
        try {
            clientDetails = clientDetailsService.loadClientByClientId(clientId);
        } catch (ClientRegistrationException e) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
        }
        if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
        }

        return clientDetails;
    }

}

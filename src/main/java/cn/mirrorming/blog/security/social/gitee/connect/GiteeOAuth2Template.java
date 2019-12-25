package cn.mirrorming.blog.security.social.gitee.connect;

import cn.mirrorming.blog.utils.JacksonUtils;
import cn.mirrorming.blog.utils.OkHttpClientUtil;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @Author Mireal
 * @Version 1.0
 */
@Slf4j
public class GiteeOAuth2Template extends OAuth2Template {
    GiteeOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    /**
     * https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
     */
    @SneakyThrows
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        log.info("{}", parameters);
        // 自己拼接url
        String clientId = parameters.getFirst("client_id");
        String clientSecret = parameters.getFirst("client_secret");
        String code = parameters.getFirst("code");
        String redirectUri = parameters.getFirst("redirect_uri");

        String url = "https://gitee.com/oauth/token";

        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put("code", code)
                .put("grant_type", "authorization_code")
                .put("client_id", clientId)
                .put("client_secret", clientSecret)
                .put("redirect_uri", redirectUri)
                .build();

        Response response = OkHttpClientUtil.getInstance().postData(url, params);

        String responseBody = Objects.requireNonNull(response.body()).string();
        log.info("获取accessToke的响应：" + responseBody);
        Map<String, Object> object = JacksonUtils.json2map(responseBody);
        String accessToken = (String) object.get("access_token");
        String scope = (String) object.get("scope");
        String refreshToken = (String) object.get("refresh_token");
        int expiresIn = (Integer) object.get("expires_in");

        log.info("Token响应:{},scope响应:{},refreshToken响应:{},expiresIn响应:{}", accessToken, scope, refreshToken, expiresIn);
        return new AccessGrant(accessToken, scope, refreshToken, (long) expiresIn);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}

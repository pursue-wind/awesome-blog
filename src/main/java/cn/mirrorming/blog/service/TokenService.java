package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.TokenDTO;
import cn.mirrorming.blog.exception.UserException;
import cn.mirrorming.blog.service.base.AbstractBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.io.IOException;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/7 21:56
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenService extends AbstractBaseService {
    private static final String URL_OAUTH_TOKEN = "http://localhost:";

    @Value("${server.port}")
    private String port;
    @Value("${system.oauth2.grant_type}")
    public String oauth2GrantType;

    @Value("${system.oauth2.client_id}")
    public String oauth2ClientId;

    @Value("${system.oauth2.client_secret}")
    public String oauth2ClientSecret;

    interface TokenRequest {
        @POST("/oauth/token")
        Call<TokenDTO> getToken(@Query("username") String username,
                                @Query("password") String password,
                                @Query("grant_type") String grant_type,
                                @Query("client_id") String client_id,
                                @Query("client_secret") String client_secret);
    }

    public TokenDTO getToken(String username, String password) {
        Retrofit retrofit = builderRetrofit(URL_OAUTH_TOKEN + port + "/oauth/token/");

        TokenRequest tokenRequest = retrofit.create(TokenRequest.class);
        Call<TokenDTO> call = tokenRequest.getToken(username, password, oauth2GrantType, oauth2ClientId, oauth2ClientSecret);
        try {
            return call.execute().body();
        } catch (IOException e) {
            log.error("登录出错:{}", e.getMessage());
            throw new UserException("登录出错！");
        }
    }
}

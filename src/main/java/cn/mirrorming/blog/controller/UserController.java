package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.dto.LoginRegisterDto;
import cn.mirrorming.blog.domain.dto.base.ResultData;
import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.exception.UserException;
import cn.mirrorming.blog.security.social.support.SocialUserInfo;
import cn.mirrorming.blog.service.UserService;
import cn.mirrorming.blog.utils.JacksonUtils;
import cn.mirrorming.blog.utils.OkHttpClientUtil;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Mireal
 * @Date 2019/9/8 11:13
 * @since v1.0.0
 */
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("user")
public class UserController {
    private static final String URL_OAUTH_TOKEN = "http://localhost:8080/oauth/token";

    @Value("${system.vue.address}")
    private String vueAddress;

    @Value("${server.port}")
    private String port;

    @Value("${system.oauth2.grant_type}")
    public String oauth2GrantType;

    @Value("${system.oauth2.client_id}")
    public String oauth2ClientId;

    @Value("${system.oauth2.client_secret}")
    public String oauth2ClientSecret;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ProviderSignInUtils providerSignInUtils;


    @GetMapping("/me")
    public Authentication me(Authentication authentication) {
        return authentication;
    }

    @PostMapping("register")
    public ResultData register(@RequestBody LoginRegisterDto loginRegisterDto) {
        boolean flag = userService.register(loginRegisterDto);
        return flag ? ResultData.succeed("注册成功") : ResultData.fail("注册失败");
    }

    @PostMapping("login")
    public ResultData login(@RequestBody LoginRegisterDto loginRegisterDto) {
        // 验证密码是否正确
        Users users = userService.selectUserByEmail(loginRegisterDto.getEmail());

        Optional.ofNullable(users).orElseThrow(() -> new UserException("用户不存在"));

        if (!passwordEncoder.matches(loginRegisterDto.getPassword(), users.getPassword())) {
            return ResultData.fail("密码不正确");
        }

        // 通过 HTTP 客户端请求登录接口
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put("password", loginRegisterDto.getPassword())
                .put("grant_type", oauth2GrantType)
                .put("client_id", oauth2ClientId)
                .put("client_secret", oauth2ClientSecret)
                .build();

        try {
            // 解析响应结果封装并返回
            Response response = OkHttpClientUtil.getInstance().postData(URL_OAUTH_TOKEN, params);
            Optional.ofNullable(response).orElseThrow(() -> new UserException("登录出错!"));

            String token = String.valueOf(
                    JacksonUtils.json2map(response.toString()).get("access_token"));

            return ResultData.succeed(token, "登录成功");
        } catch (Exception e) {
            log.error("登录出错:{}", e.getMessage());
            throw new UserException("登录出错！");
        }
    }

    /**
     * 保存完信息然后跳转到绑定用户信息页面
     */
    @GetMapping("/socialSignUp")
    public void getSocialUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        SocialUserInfo socialUserInfo = SocialUserInfo.builder()
                .providerId(connection.getKey().getProviderId())
                .providerUserId(connection.getKey().getProviderUserId())
                .nickname(connection.getDisplayName())
                .headimg(connection.getProfileUrl())
                .build();
        // 跳转到用户绑定页面
        response.sendRedirect(vueAddress + "/bind?key=" + connection.getKey().getProviderId());
    }
}


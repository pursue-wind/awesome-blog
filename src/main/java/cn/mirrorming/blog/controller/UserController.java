package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.utils.RedisOperator;
import cn.mirrorming.blog.domain.dto.LoginRegisterDTO;
import cn.mirrorming.blog.domain.dto.TokenDTO;
import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.dto.user.UserDTO;
import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.exception.UserException;
import cn.mirrorming.blog.security.MyUserDetails;
import cn.mirrorming.blog.security.social.support.SocialUserInfo;
import cn.mirrorming.blog.service.TokenService;
import cn.mirrorming.blog.service.UserService;
import cn.mirrorming.blog.utils.JacksonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Mireal Chan
 * @Date 2019/9/8 11:13
 * @since v1.0.0
 */
@Api(tags = "用户")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("user")
public class UserController {
    @Value("${config.vue.address}")
    private String vueAddress;
    private final UserService userService;
    private final TokenService tokenService;
    private final RedisOperator redisOperator;
    private final PasswordEncoder passwordEncoder;
    private final ProviderSignInUtils providerSignInUtils;

    @ApiOperation(value = "me")
    @GetMapping("/me")
    public R me(Authentication authentication) {
        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
        return R.succeed(UserDTO.builder()
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .name(user.getUsername()).build());
    }

    @ApiOperation(value = "注册")
    @PostMapping("register")
    public R register(@RequestBody LoginRegisterDTO loginRegisterDto) {
        userService.register(loginRegisterDto);
        return R.succeed("注册成功");
    }

    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R login(@RequestBody LoginRegisterDTO loginRegisterDto) {
        // 验证密码是否正确
        Users user = userService.selectUserByEmail(loginRegisterDto.getEmail());
        Optional.ofNullable(user).orElseThrow(() -> new UserException("用户不存在"));
        if (!passwordEncoder.matches(loginRegisterDto.getPassword(), user.getPassword())) {
            return R.fail("密码不正确");
        }
        TokenDTO token = tokenService.getToken(loginRegisterDto.getEmail(), loginRegisterDto.getPassword());
        return R.succeed(UserDTO.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .email(user.getEmail())
                .token(token.getAccess_token())
                .build(), "注册成功！");
    }

    /**
     * 社交登录跳转到绑定页面
     *
     * @return
     */
    @ApiOperation(value = "社交登录跳转到绑定页面")
    @GetMapping("social")
    public void social(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        String uuid = UUID.randomUUID().toString();
        SocialUserInfo socialUserInfo = SocialUserInfo.builder()
                .providerId(connection.getKey().getProviderId())
                .providerUserId(connection.getKey().getProviderUserId())
                .nickname(connection.getDisplayName())
                .headimg(connection.getImageUrl())
                .build();
        ConnectionData connectionData = connection.createData();
        redisOperator.set(uuid, JacksonUtils.obj2json(connectionData));
        // 跳转到用户绑定页面
        response.sendRedirect(vueAddress + "/login?bindSocial=" + uuid + "&username=" + socialUserInfo.getNickname() + "&avatar=" + socialUserInfo.getHeadimg());
    }

    /**
     * 社交登录绑定并注册账户，返回账户信息和 token
     *
     * @return
     */
    @ApiOperation(value = "社交登录绑定并注册账户")
    @PostMapping("social/register")
    public R socialRegister(@RequestBody LoginRegisterDTO loginRegisterDto) throws Exception {
        Users user = userService.bindSocialInfoAndSignUp(loginRegisterDto);
        TokenDTO token = tokenService.getToken(loginRegisterDto.getEmail(), loginRegisterDto.getPassword());
        return R.succeed(UserDTO.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .email(user.getEmail())
                .token(token.getAccess_token())
                .build(), "注册成功！");
    }
}


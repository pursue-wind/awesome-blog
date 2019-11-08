package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.dto.base.ResultData;
import cn.mirrorming.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

/**
 * @Author mirror
 * @Date 2019/9/8 11:13
 * @since v1.0.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    //todo 只能通过邮箱注册
    private final UserService userService;


    @GetMapping("/me")
    public Authentication me(Authentication authentication) {
        return authentication;
    }

    @PostMapping
    public ResultData register(@RequestBody User user) {

        return null;
    }
}


package cn.mirrorming.blog.security;

import cn.mirrorming.blog.domain.po.SecurityPermission;
import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.exception.UserException;
import cn.mirrorming.blog.service.PermissionService;
import cn.mirrorming.blog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author mirror
 * @Date 2019/9/4 16:54
 * @since v1.0.0
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("用户登录邮箱：{}", email);
        // 查询用户信息
        Users user = userService.selectUserByEmail(email);
        Optional.ofNullable(user).orElseThrow(() -> new UserException("用户不存在"));

        List<GrantedAuthority> grantedAuthorities = new CopyOnWriteArrayList<>();
        // 获取用户授权
        List<SecurityPermission> securityPermissions = permissionService.selectPermissionByUserId(user.getId());
        // 声明用户授权
        securityPermissions.parallelStream()
                .filter(permission -> permission.getEnname() != null)
                .forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission.getEnname())));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 由框架完成认证工作
        return new User(
                user.getName(),
                user.getPassword(),
                true, true, true, true,
                grantedAuthorities);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        log.info("social 登录：{}", s);

        // 查询用户信息
        Users user = userService.selectUserByEmail(s);
        Optional.ofNullable(user).orElseThrow(() -> new UserException("用户不存在"));

        List<GrantedAuthority> grantedAuthorities = new CopyOnWriteArrayList<>();
        // 获取用户授权
        List<SecurityPermission> securityPermissions = permissionService.selectPermissionByUserId(user.getId());
        // 声明用户授权
        securityPermissions.parallelStream()
                .filter(permission -> permission.getEnname() != null)
                .forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission.getEnname())));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 由框架完成认证工作
        return new SocialUser(
                user.getName(),
                user.getPassword(),
                true, true, true, true,
                grantedAuthorities);
    }
}
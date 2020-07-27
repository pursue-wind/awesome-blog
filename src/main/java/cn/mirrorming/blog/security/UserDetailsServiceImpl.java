package cn.mirrorming.blog.security;


import cn.mirrorming.blog.domain.po.Permission;
import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.exception.UserException;
import cn.mirrorming.blog.mapper.PermissionQueryMapper;
import cn.mirrorming.blog.mapper.generate.UsersMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.security.SecurityPermission;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static cn.mirrorming.blog.domain.enums.RespEnum.USER_NOT_EXIST;

/**
 * @author Mireal
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService, SocialUserDetailsService {

    private final UsersMapper usersMapper;
    private final PermissionQueryMapper permissionQueryMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 查询用户信息
        Users user = loginByPhone(s);
        return getUserDetails(user);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String email) throws UsernameNotFoundException {
        log.info("social 登录：{}", email);
        // 查询用户信息
        Users user = loginByEmail(email);
        return getUserDetails(user);
    }

    private MyUserDetails getUserDetails(Users user) {
        Optional.ofNullable(user).orElseThrow(() -> new AppException(USER_NOT_EXIST));
        List<Permission> permissionList = permissionQueryMapper.selectPermissionByUserId(user.getId());
        // 声明用户授权
        List<GrantedAuthority> grantedAuthorities = permissionList.parallelStream()
                .filter(permission -> permission.getValue() != null)
                .map(p -> new SimpleGrantedAuthority(p.getValue()))
                .collect(Collectors.toList());
        // 由框架完成认证工作
        return MyUserDetails.of(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAvatar(),
                user.getPassword(),
                user.getIsFreeze(),
                grantedAuthorities);
    }

    private Users loginByPhone(String phone) {
        return usersMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getPhone, phone).last("LIMIT 1"));
    }

    private Users loginByEmail(String email) {
        return usersMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getEmail, email).last("LIMIT 1"));
    }
}
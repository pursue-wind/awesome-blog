package cn.mirrorming.blog.security.service;

import com.google.common.collect.Lists;
import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.domain.po.SecurityPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author mirror
 * @Date 2019/9/4 16:54
 * @since v1.0.0
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PermissionService permissionService;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // 查询用户信息
//        Users users = usersService.getByUsername(username);
//        List<GrantedAuthority> grantedAuthorities = new CopyOnWriteArrayList<>();
//        if (users != null) {
//            // 获取用户授权
//            List<SecurityPermission> securityPermissions = permissionService.selectPermissionByUserId(users.getId());
//
//            // 声明用户授权
//            securityPermissions.parallelStream().forEach(permission -> {
//                if (permission != null && permission.getEnname() != null) {
//                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getEnname());
//                    grantedAuthorities.add(grantedAuthority);
//                }
//            });
//        }
//        //
//        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        // 由框架完成认证工作
//        return new Users(
//                users.getName(),
//                users.getPassword(),
//                true, true, true, true,
//                grantedAuthorities);
//    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 查询用户
        Users user = usersService.getByUsername(s);

        // 默认所有用户拥有 USER 权限
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        grantedAuthorities.add(grantedAuthority);

        // 用户存在
        if (user != null) {
            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        }

        // 用户不存在
        else {
            return null;
        }
    }
}
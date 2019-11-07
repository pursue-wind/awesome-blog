package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.exception.RegisterException;
import cn.mirrorming.blog.mapper.auto.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/7 21:56
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersMapper.insert(user);
    }

    /**
     * 查询用户名是否已存在
     *
     * @param name
     * @return Users
     * @throws RegisterException 用户名已存在则抛异常
     */
    public Users selectUserByUserName(String name) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq(Users.COL_NAME, name));
        Optional.ofNullable(users).orElseGet(Users::new);
        return users;
    }
}

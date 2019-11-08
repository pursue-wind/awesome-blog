package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.constants.SystemConstant;
import cn.mirrorming.blog.domain.dto.LoginRegisterDto;
import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.exception.UserException;
import cn.mirrorming.blog.mapper.auto.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
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

    /**
     * 用户注册
     *
     * @param loginRegisterDto
     * @return
     */
    public boolean register(LoginRegisterDto loginRegisterDto) {
        Users users = this.selectUserByUserName(loginRegisterDto.getEmail());
        if (users != null) {
            throw new UserException("邮箱已存在！");
        }
        return usersMapper.insert(
                Users.builder()
                        .email(loginRegisterDto.getEmail())
                        .password(passwordEncoder.encode(loginRegisterDto.getPassword()))
                        .build()) == SystemConstant.EFFECT_ROW;
    }

    /**
     * 查询用户名是否已存在
     *
     * @param name
     * @return Users
     * @throws UserException 用户名已存在则抛异常
     */
    public Users selectUserByUserName(String name) {
        Users users = usersMapper.selectOne(new QueryWrapper<Users>().eq(Users.COL_NAME, name));
        Optional.ofNullable(users).orElseGet(Users::new);
        return users;
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email
     * @return
     */
    public Users selectUserByEmail(String email) {
        return usersMapper.selectOne(new QueryWrapper<Users>().eq(Users.COL_EMAIL, email));
    }
}

package cn.mirrorming.blog.service;

import cn.mirrorming.blog.utils.RedisOperator;
import cn.mirrorming.blog.domain.dto.LoginRegisterDTO;
import cn.mirrorming.blog.domain.po.UserRoleRelation;
import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.exception.UserException;
import cn.mirrorming.blog.mapper.generate.UserRoleRelationMapper;
import cn.mirrorming.blog.mapper.generate.UsersMapper;
import cn.mirrorming.blog.utils.Check;
import cn.mirrorming.blog.utils.JacksonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cn.mirrorming.blog.domain.enums.RespEnum.REGISTER_FAIL;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/7 21:56
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    public static final int REGISTER_USER_ROLE_ID = 3;
    private final UsersMapper usersMapper;
    private final UserRoleRelationMapper userRoleRelationMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisOperator redisOperator;
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final UsersConnectionRepository usersConnectionRepository;

    /**
     * 用户注册
     *
     * @param loginRegisterDto
     * @return
     */
    public void register(LoginRegisterDTO loginRegisterDto) {
        Users users = this.selectUserByUserName(loginRegisterDto.getEmail());
        if (users != null) {
            throw new UserException("邮箱已存在！");
        }
        int insert = usersMapper.insert(
                Users.builder()
                        .email(loginRegisterDto.getEmail())
                        .password(passwordEncoder.encode(loginRegisterDto.getPassword()))
                        .build());
        Check.affectedOneRow(insert).orElseThrow(() -> new AppException(REGISTER_FAIL));
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

    /**
     * 通过id查询用户
     *
     * @param id
     * @return
     */
    public Users selectUserById(Integer id) {
        return usersMapper.selectById(id);
    }

    /**
     * 将业务系统的用户与社交用户绑定
     *
     * @param loginRegisterDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Users bindSocialInfoAndSignUp(LoginRegisterDTO loginRegisterDto) throws Exception {
        // 进行账号校验
        Users users = this.selectUserByUserName(loginRegisterDto.getEmail());
        if (users != null) {
            throw new UserException("邮箱已存在！");
        }
        String key = loginRegisterDto.getKey();
        if (!redisOperator.hasKey(key)) {
            throw new AppException("无法找到缓存的第三方社交账号信息");
        }
        String connectionDataJson = redisOperator.get(key);
        ConnectionData connectionData = JacksonUtils.json2pojo(connectionDataJson, ConnectionData.class);
        Optional.ofNullable(connectionData).orElseThrow(() -> new AppException("无法找到缓存的第三方社交账号信息"));

        //从connectionData获得用户的信息并构建用户
        Users user = Users.builder()
                .name(connectionData.getDisplayName())
                .email(loginRegisterDto.getEmail())
                .password(passwordEncoder.encode(loginRegisterDto.getPassword()))
                .avatar(connectionData.getImageUrl())
                .build();
        usersMapper.insert(user);
        log.info("社交账号注册用户，{}", user);
        UserRoleRelation securityUserRole = UserRoleRelation.builder().roleId(REGISTER_USER_ROLE_ID).userId(user.getId()).build();
        userRoleRelationMapper.insert(securityUserRole);
        log.info("社交账号注册用户添加权限，{}", securityUserRole);
        //添加到userConnection表
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId()).createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(String.valueOf(user.getEmail())).addConnection(connection);
        redisOperator.delete(key);
        return user;
    }
}

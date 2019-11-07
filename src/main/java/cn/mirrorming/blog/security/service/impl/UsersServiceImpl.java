package cn.mirrorming.blog.security.service.impl;

import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.mapper.auto.UsersMapper;
import cn.mirrorming.blog.security.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Resource
    private UsersMapper usersMapper;

    @Override
    public Users getByUsername(String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        return usersMapper.selectOne(queryWrapper);
    }
}


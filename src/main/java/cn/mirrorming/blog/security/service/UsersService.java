package cn.mirrorming.blog.security.service;

import cn.mirrorming.blog.domain.po.Users;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UsersService extends IService<Users> {

    Users getByUsername(String username);
}


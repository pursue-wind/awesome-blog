package com.minzheng.blog.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.dao.UserRoleDao
import com.minzheng.blog.entity.UserRole
import org.springframework.stereotype.Service

/**
 * 用户角色服务
 *
 * @author c
 * @date 2021/08/10
 */
@Service
class UserRoleService : ServiceImpl<UserRoleDao, UserRole>()
package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.UserRole
import org.springframework.stereotype.Repository

/**
 * 用户角色
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface UserRoleDao : BaseMapper<UserRole>
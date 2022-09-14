package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.RoleMenu
import org.springframework.stereotype.Repository

/**
 * 角色菜单
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface RoleMenuDao : BaseMapper<RoleMenu>
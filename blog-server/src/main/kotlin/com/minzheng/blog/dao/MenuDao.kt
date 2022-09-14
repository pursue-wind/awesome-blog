package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.Menu
import org.springframework.stereotype.Repository

/**
 * 菜单
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface MenuDao : BaseMapper<Menu> {
    /**
     * 根据用户id查询菜单
     * @param userInfoId 用户信息id
     * @return 菜单列表
     */
    fun listMenusByUserInfoId(userInfoId: Int?): List<Menu>?
}
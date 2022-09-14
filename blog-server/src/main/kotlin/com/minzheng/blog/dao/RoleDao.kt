package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.dto.ResourceRoleDTO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.RoleDTO
import com.minzheng.blog.entity.Role
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 角色
 *
 * @author c
 * @date 2021/07/28
 */
@Repository
interface RoleDao : BaseMapper<Role> {
    /**
     * 查询路由角色列表
     *
     * @return 角色标签
     */
    fun listResourceRoles(): List<ResourceRoleDTO>?

    /**
     * 根据用户id获取角色列表
     *
     * @param userInfoId 用户id
     * @return 角色标签
     */
    fun listRolesByUserInfoId(userInfoId: Int?): List<String>?

    /**
     * 查询角色列表
     *
     * @param current     页码
     * @param size        条数
     * @param conditionVO 条件
     * @return 角色列表
     */
    fun listRoles(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("conditionVO") conditionVO: ConditionVO?
    ): List<RoleDTO>?
}
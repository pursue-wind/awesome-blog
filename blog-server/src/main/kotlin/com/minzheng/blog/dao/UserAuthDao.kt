package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.UserAuth
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.UserBackDTO
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 用户账号
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface UserAuthDao : BaseMapper<UserAuth> {
    /**
     * 查询后台用户列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return [<] 用户列表
     */
    fun listUsers(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("condition") condition: ConditionVO?
    ): List<UserBackDTO>?

    /**
     * 查询后台用户数量
     *
     * @param condition 条件
     * @return 用户数量
     */
    fun countUser(@Param("condition") condition: ConditionVO?): Long?
}
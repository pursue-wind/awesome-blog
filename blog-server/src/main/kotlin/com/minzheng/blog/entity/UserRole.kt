package com.minzheng.blog.entity

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableName
import lombok.Builder
import lombok.Data

/**
 * 用户角色
 *
 * @author c
 * @date 2021/07/29
 */
@TableName("tb_user_role")
class UserRole {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Int? = null

    /**
     * 用户id
     */
    var userId: Int? = null

    /**
     * 角色id
     */
    var roleId: Int? = null
}
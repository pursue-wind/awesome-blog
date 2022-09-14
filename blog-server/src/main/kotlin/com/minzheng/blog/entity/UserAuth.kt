package com.minzheng.blog.entity

import com.baomidou.mybatisplus.annotation.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.Builder
import lombok.Data
import java.time.LocalDateTime

/**
 * 用户账号
 *
 * @author c
 * @date 2021/08/01
 * @since 2020-05-18
 */
@TableName("tb_user_auth")
class UserAuth {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Int? = null

    /**
     * 用户信息id
     */
    var userInfoId: Int? = null

    /**
     * 用户名
     */
    var username: String? = null

    /**
     * 密码
     */
    var password: String? = null

    /**
     * 登录类型
     */
    var loginType: Int? = null

    /**
     * 用户登录ip
     */
    var ipAddress: String? = null

    /**
     * ip来源
     */
    var ipSource: String? = null

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    var createTime: LocalDateTime? = null

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    var updateTime: LocalDateTime? = null

    /**
     * 最近登录时间
     */
    var lastLoginTime: LocalDateTime? = null
}
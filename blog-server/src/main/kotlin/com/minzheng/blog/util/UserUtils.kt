package com.minzheng.blog.util

import com.minzheng.blog.dto.UserDetailDTO
import org.springframework.security.core.context.SecurityContextHolder

/**
 * 用户工具类
 *
 * @author c
 * @date 2021/08/10
 */
object UserUtils {
    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    @JvmStatic
    fun getLoginUser(): UserDetailDTO = SecurityContextHolder.getContext().authentication.principal as UserDetailDTO
}
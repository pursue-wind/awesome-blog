package com.minzheng.blog.handler

import com.alibaba.fastjson.JSON
import com.minzheng.blog.constant.CommonConst.APPLICATION_JSON
import com.minzheng.blog.dao.UserAuthDao
import com.minzheng.blog.dto.UserInfoDTO
import com.minzheng.blog.entity.UserAuth
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.util.UserUtils
import com.minzheng.blog.vo.Result
import org.springframework.scheduling.annotation.Async
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 登录成功处理
 *
 * @author c
 * @date 2021/07/28
 */
@Component
class AuthenticationSuccessHandler(
    private val userAuthDao: UserAuthDao
) : AuthenticationSuccessHandler {

    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        authentication: Authentication
    ) {
        // 返回登录信息
        val userLoginDTO = BeanCopyUtils.copyObject(UserUtils.getLoginUser(), UserInfoDTO::class.java)
        httpServletResponse.contentType = APPLICATION_JSON
        httpServletResponse.writer.write(JSON.toJSONString(Result.ok(userLoginDTO)))
        // 更新用户ip，最近登录时间
        updateUserInfo()
    }

    /**
     * 更新用户信息
     */
    @Async
    fun updateUserInfo() {
        val userAuth: UserAuth = UserAuth().apply {
            id = UserUtils.getLoginUser().id
            ipAddress = UserUtils.getLoginUser().ipAddress
            ipSource = UserUtils.getLoginUser().ipSource
            lastLoginTime = UserUtils.getLoginUser().lastLoginTime
        }

        userAuthDao!!.updateById(userAuth)
    }
}
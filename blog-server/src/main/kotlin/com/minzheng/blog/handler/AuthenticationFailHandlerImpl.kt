package com.minzheng.blog.handler

import com.alibaba.fastjson.JSON
import com.minzheng.blog.constant.CommonConst
import com.minzheng.blog.vo.Result
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import kotlin.Throws
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 登录失败处理
 *
 * @author c
 * @date 2021/07/28
 */
@Component
class AuthenticationFailHandlerImpl : AuthenticationFailureHandler {
    @Throws(IOException::class)
    override fun onAuthenticationFailure(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        e: AuthenticationException
    ) {
        httpServletResponse.contentType = CommonConst.APPLICATION_JSON
        httpServletResponse.writer.write(JSON.toJSONString(Result.fail<Any>(e.message)))
    }
}
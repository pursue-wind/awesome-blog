package com.minzheng.blog.handler

import com.alibaba.fastjson.JSON
import org.springframework.security.web.AuthenticationEntryPoint
import kotlin.Throws
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.minzheng.blog.constant.CommonConst
import com.minzheng.blog.enums.StatusCodeEnum
import com.minzheng.blog.vo.Result
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

/**
 * 用户未登录处理
 *
 * @author c
 */
@Component
class AuthenticationEntryPointImpl : AuthenticationEntryPoint {
    @Throws(IOException::class)
    override fun commence(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        e: AuthenticationException
    ) {
        httpServletResponse.contentType = CommonConst.APPLICATION_JSON
        httpServletResponse.writer.write(JSON.toJSONString(Result.fail<StatusCodeEnum>(StatusCodeEnum.NO_LOGIN)))
    }
}
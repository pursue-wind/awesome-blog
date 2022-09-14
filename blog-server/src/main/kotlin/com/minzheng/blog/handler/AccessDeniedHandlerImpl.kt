package com.minzheng.blog.handler

import com.alibaba.fastjson.JSON
import org.springframework.security.web.access.AccessDeniedHandler
import kotlin.Throws
import java.io.IOException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.minzheng.blog.constant.CommonConst
import com.minzheng.blog.vo.Result
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component

/**
 * 用户权限处理
 *
 * @author c
 */
@Component
class AccessDeniedHandlerImpl : AccessDeniedHandler {
    @Throws(IOException::class)
    override fun handle(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        e: AccessDeniedException
    ) {
        httpServletResponse.contentType = CommonConst.APPLICATION_JSON
        httpServletResponse.writer.write(JSON.toJSONString(Result.fail<Any>("权限不足")))
    }
}
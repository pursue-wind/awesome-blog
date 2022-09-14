package com.minzheng.blog.handler

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.minzheng.blog.constant.CommonConst
import com.minzheng.blog.constant.CommonConst.DEFAULT_SIZE
import com.minzheng.blog.util.PageUtils
import org.springframework.lang.Nullable
import org.springframework.util.StringUtils
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 分页拦截器
 *
 * @author c
 * @date 2021/07/18
 */
class PageableHandlerInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val currentPage = request.getParameter(CommonConst.CURRENT)
        val pageSize = request.getParameter(CommonConst.SIZE) ?: DEFAULT_SIZE
        if (StringUtils.hasText(currentPage)) {
            PageUtils.setCurrentPage(Page<Any>(currentPage.toLong(), pageSize.toLong()))
        }
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        @Nullable ex: Exception?
    ) {
        PageUtils.remove()
    }
}
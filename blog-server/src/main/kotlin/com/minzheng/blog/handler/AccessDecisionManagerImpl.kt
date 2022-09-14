package com.minzheng.blog.handler

import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDeniedException
import kotlin.Throws
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.util.stream.Collectors

/**
 * 访问决策管理器
 *
 * @author c
 */
@Component
class AccessDecisionManagerImpl : AccessDecisionManager {
    @Throws(AccessDeniedException::class, InsufficientAuthenticationException::class)
    override fun decide(authentication: Authentication, o: Any, collection: Collection<ConfigAttribute>) {
        // 获取用户权限列表
        val permissionList = authentication.authorities.map { it.authority }
        for (item in collection) {
            if (permissionList.contains(item.attribute)) {
                return
            }
        }
        throw AccessDeniedException("没有操作权限")
    }

    override fun supports(configAttribute: ConfigAttribute): Boolean {
        return true
    }

    override fun supports(aClass: Class<*>?): Boolean {
        return true
    }
}
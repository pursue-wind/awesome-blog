package com.minzheng.blog.aspect

import com.alibaba.fastjson.JSON
import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.dao.OperationLogDao
import com.minzheng.blog.entity.OperationLog
import com.minzheng.blog.util.IpUtils
import com.minzheng.blog.util.UserUtils
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * 操作日志切面处理
 *
 * @author c
 * @date 2021/07/27
 */
@Aspect
@Component
class OptLogAspect(
    private val operationLogDao: OperationLogDao
) {

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.minzheng.blog.annotation.OptLog)")
    fun optLogPointCut() {
    }

    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * 使用GET、POST、PUT、DELETE4个表示操作方式的动词对服务端资源进行操作
     * GET用来获取资源
     * POST用来新建资源（也可以用于更新资源）
     * PUT用来更新资源
     * DELETE用来删除资源
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    fun saveOptLog(joinPoint: JoinPoint, keys: Any?) {
        // 获取RequestAttributes
        val requestAttributes = RequestContextHolder.getRequestAttributes()
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        val request = Objects.requireNonNull(requestAttributes)
            ?.resolveReference(RequestAttributes.REFERENCE_REQUEST) as HttpServletRequest
        val operationLog = OperationLog()
        // 从切面织入点处通过反射机制获取织入点处的方法
        val signature = joinPoint.signature as MethodSignature
        // 获取切入点所在的方法
        val method = signature.method
        // 获取操作
        val api = signature.declaringType.getAnnotation<Api>(Api::class.java)
        val apiOperation = method.getAnnotation(ApiOperation::class.java)
        val optLog = method.getAnnotation(OptLog::class.java)
        // 操作模块
        operationLog.optModule = api?.let { (it as Api).tags[0] }
        // 操作类型
        operationLog.optType = optLog.optType
        // 操作描述
        operationLog.optDesc = apiOperation.value
        // 获取请求的类名
        val className = joinPoint.target.javaClass.name
        // 获取请求的方法名
        var methodName = method.name
        methodName = "$className.$methodName"
        // 请求方式
        operationLog.requestMethod = Objects.requireNonNull(request).method
        // 请求方法
        operationLog.optMethod = methodName
        // 请求参数
        operationLog.requestParam = JSON.toJSONString(joinPoint.args)
        // 返回结果
        operationLog.responseData = JSON.toJSONString(keys)
        // 请求用户ID
        operationLog.userId = UserUtils.getLoginUser().id
        // 请求用户
        operationLog.nickname = UserUtils.getLoginUser().nickname
        // 请求IP
        val ipAddress = IpUtils.getIpAddress(request)
        operationLog.ipAddress = ipAddress
        operationLog.ipSource = ipAddress?.let { IpUtils.getIpSource(it) }
        // 请求URL
        operationLog.optUrl = request.requestURI
        operationLogDao.insert(operationLog)
    }
}
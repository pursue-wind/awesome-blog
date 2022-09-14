package com.minzheng.blog.handler

import com.minzheng.blog.enums.StatusCodeEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.vo.Result
import lombok.extern.log4j.Log4j2
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

/**
 * 全局异常处理
 *
 * @author yezhqiu
 * @date 2021/06/11
 */
@Log4j2
@RestControllerAdvice
class ControllerAdviceHandler {
    /**
     * 处理服务异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = [BizException::class])
    fun errorHandler(e: BizException): Result<*> {
        return Result.fail<Any>(e.code, e.message)
    }

    /**
     * 处理参数校验异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun errorHandler(e: MethodArgumentNotValidException): Result<*> {
        return Result.fail<Any>(
            StatusCodeEnum.VALID_ERROR.code,
            Objects.requireNonNull(e.bindingResult.fieldError)?.defaultMessage
        )
    }

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = [Exception::class])
    fun errorHandler(e: Exception): Result<*> {
        e.printStackTrace()
        return Result.fail<Any>(StatusCodeEnum.SYSTEM_ERROR.code, StatusCodeEnum.SYSTEM_ERROR.desc)
    }
}
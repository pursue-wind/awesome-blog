package com.minzheng.blog.controller

import io.swagger.annotations.Api
import com.minzheng.blog.service.OperationLogService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.OperationLogDTO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.Result
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * 日志控制器
 *
 * @author c
 * @date 2021/07/27
 */
@Api(tags = ["日志模块"])
@RestController
class LogController(
    private val operationLogService: OperationLogService
) {
    /**
     * 查看操作日志
     *
     * @param conditionVO 条件
     * @return [<] 日志列表
     */
    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/operation/logs")
    fun listOperationLogs(conditionVO: ConditionVO?): Result<PageResult<OperationLogDTO>> {
        return Result.ok(operationLogService.listOperationLogs(conditionVO))
    }

    /**
     * 删除操作日志
     *
     * @param logIdList 日志id列表
     * @return [<]
     */
    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/admin/operation/logs")
    fun deleteOperationLogs(@RequestBody logIdList: List<Int>?): Result<*> {
        operationLogService.removeByIds(logIdList)
        return Result.ok<Any>()
    }
}
package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.dto.MessageBackDTO
import com.minzheng.blog.dto.MessageDTO
import com.minzheng.blog.service.MessageService
import com.minzheng.blog.vo.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * 留言控制器
 *
 * @author xiaojie
 * @date 2021/07/29
 */
@Api(tags = ["留言模块"])
@RestController
class MessageController(
    private val messageService: MessageService
) {

    /**
     * 添加留言
     *
     * @param messageVO 留言信息
     * @return [<]
     */
    @ApiOperation(value = "添加留言")
    @PostMapping("/messages")
    fun saveMessage(@RequestBody messageVO: @Valid MessageVO): Result<*> {
        messageService.saveMessage(messageVO)
        return Result.ok<Any>()
    }

    /**
     * 查看留言列表
     *
     * @return [<] 留言列表
     */
    @ApiOperation(value = "查看留言列表")
    @GetMapping("/messages")
    fun listMessages(): Result<List<MessageDTO>> {
        return Result.ok(messageService.listMessages())
    }

    /**
     * 查看后台留言列表
     *
     * @param condition 条件
     * @return [<] 留言列表
     */
    @ApiOperation(value = "查看后台留言列表")
    @GetMapping("/admin/messages")
    fun listMessageBackDTO(condition: ConditionVO): Result<PageResult<MessageBackDTO>> {
        return Result.ok(messageService.listMessageBackDTO(condition))
    }

    /**
     * 审核留言
     *
     * @param reviewVO 审核信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "审核留言")
    @PutMapping("/admin/messages/review")
    fun updateMessagesReview(@RequestBody reviewVO: @Valid ReviewVO): Result<*> {
        messageService.updateMessagesReview(reviewVO)
        return Result.ok<Any>()
    }

    /**
     * 删除留言
     *
     * @param messageIdList 留言id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除留言")
    @DeleteMapping("/admin/messages")
    fun deleteMessages(@RequestBody messageIdList: List<Int>): Result<*> {
        messageService.removeByIds(messageIdList)
        return Result.ok<Any>()
    }
}
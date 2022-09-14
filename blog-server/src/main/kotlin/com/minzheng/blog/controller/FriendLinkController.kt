package com.minzheng.blog.controller

import com.minzheng.blog.service.FriendLinkService
import io.swagger.annotations.ApiOperation
import com.minzheng.blog.dto.FriendLinkDTO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.FriendLinkBackDTO
import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import javax.validation.Valid
import com.minzheng.blog.vo.FriendLinkVO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.Result
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*

/**
 * 友链控制器
 *
 * @author c
 * @date 2021/07/29
 */
@Api(tags = ["友链模块"])
@RestController
class FriendLinkController(
    private val friendLinkService: FriendLinkService
) {

    /**
     * 查看友链列表
     *
     * @return [<] 友链列表
     */
    @ApiOperation(value = "查看友链列表")
    @GetMapping("/links")
    fun listFriendLinks(): Result<List<FriendLinkDTO>> {
        return Result.ok(friendLinkService.listFriendLinks())
    }

    /**
     * 查看后台友链列表
     *
     * @param condition 条件
     * @return [<] 后台友链列表
     */
    @ApiOperation(value = "查看后台友链列表")
    @GetMapping("/admin/links")
    fun listFriendLinkDTO(condition: ConditionVO?): Result<PageResult<FriendLinkBackDTO>> {
        return Result.ok(friendLinkService.listFriendLinkDTO(condition))
    }

    /**
     * 保存或修改友链
     *
     * @param friendLinkVO 友链信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改友链")
    @PostMapping("/admin/links")
    fun saveOrUpdateFriendLink(@RequestBody friendLinkVO: @Valid FriendLinkVO?): Result<*> {
        friendLinkService.saveOrUpdateFriendLink(friendLinkVO)
        return Result.ok<Any>()
    }

    /**
     * 删除友链
     *
     * @param linkIdList 友链id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除友链")
    @DeleteMapping("/admin/links")
    fun deleteFriendLink(@RequestBody linkIdList: List<Int>?): Result<*> {
        friendLinkService.removeByIds(linkIdList)
        return Result.ok<Any>()
    }
}
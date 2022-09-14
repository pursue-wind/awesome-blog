package com.minzheng.blog.controller

import com.minzheng.blog.dto.TalkBackDTO
import com.minzheng.blog.dto.TalkDTO
import com.minzheng.blog.enums.FilePathEnum
import com.minzheng.blog.service.TalkService
import com.minzheng.blog.strategy.context.UploadStrategyContext
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.Result
import com.minzheng.blog.vo.TalkVO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

/**
 * 说说控制器
 *
 * @author c
 * @date 2022/01/23
 */
@Api(tags = ["说说模块"])
@RestController
class TalkController(
    private val talkService: TalkService,
    private val uploadStrategyContext: UploadStrategyContext
) {

    /**
     * 查看首页说说
     *
     * @return [<]
     */
    @ApiOperation(value = "查看首页说说")
    @GetMapping("/home/talks")
    fun listHomeTalks(): Result<List<String>> {
        return Result.ok(talkService!!.listHomeTalks())
    }

    /**
     * 查看说说列表
     *
     * @return [<]
     */
    @ApiOperation(value = "查看说说列表")
    @GetMapping("/talks")
    fun listTalks(): Result<PageResult<TalkDTO>> {
        return Result.ok(talkService!!.listTalks())
    }

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return [<]
     */
    @ApiOperation(value = "根据id查看说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @GetMapping("/talks/{talkId}")
    fun getTalkById(@PathVariable("talkId") talkId: Int?): Result<TalkDTO> {
        return Result.ok(talkService!!.getTalkById(talkId))
    }

    /**
     * 点赞说说
     *
     * @param talkId 说说id
     * @return [<]
     */
    @ApiOperation(value = "点赞说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @PostMapping("/talks/{talkId}/like")
    fun saveTalkLike(@PathVariable("talkId") talkId: Int?): Result<*> {
        talkService!!.saveTalkLike(talkId)
        return Result.ok<Any>()
    }

    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return [<] 说说图片地址
     */
    @ApiOperation(value = "上传说说图片")
    @ApiImplicitParam(name = "file", value = "说说图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/talks/images")
    fun saveTalkImages(file: MultipartFile?): Result<String> {
        return Result.ok(uploadStrategyContext!!.executeUploadStrategy(file, FilePathEnum.TALK.path))
    }

    /**
     * 保存或修改说说
     *
     * @param talkVO 说说信息
     * @return [<]
     */
    @ApiOperation(value = "保存或修改说说")
    @PostMapping("/admin/talks")
    fun saveOrUpdateTalk(@RequestBody talkVO: @Valid TalkVO?): Result<*> {
        talkService!!.saveOrUpdateTalk(talkVO)
        return Result.ok<Any>()
    }

    /**
     * 删除说说
     *
     * @param talkIdList 说说id列表
     * @return [<]
     */
    @ApiOperation(value = "删除说说")
    @DeleteMapping("/admin/talks")
    fun deleteTalks(@RequestBody talkIdList: List<Int>?): Result<*> {
        talkService!!.deleteTalks(talkIdList)
        return Result.ok<Any>()
    }

    /**
     * 查看后台说说
     *
     * @param conditionVO 条件
     * @return [<] 说说列表
     */
    @ApiOperation(value = "查看后台说说")
    @GetMapping("/admin/talks")
    fun listBackTalks(conditionVO: ConditionVO?): Result<PageResult<TalkBackDTO>> {
        return Result.ok(talkService!!.listBackTalks(conditionVO))
    }

    /**
     * 根据id查看后台说说
     *
     * @param talkId 说说id
     * @return [<]
     */
    @ApiOperation(value = "根据id查看后台说说")
    @ApiImplicitParam(name = "talkId", value = "说说id", required = true, dataType = "Integer")
    @GetMapping("/admin/talks/{talkId}")
    fun getBackTalkById(@PathVariable("talkId") talkId: Int?): Result<TalkBackDTO> {
        return Result.ok(talkService!!.getBackTalkById(talkId))
    }
}
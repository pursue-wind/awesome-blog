package com.minzheng.blog.controller

import com.minzheng.blog.service.TagService
import io.swagger.annotations.ApiOperation
import com.minzheng.blog.dto.TagDTO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.TagBackDTO
import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.Result
import javax.validation.Valid
import com.minzheng.blog.vo.TagVO
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*

/**
 * 标签控制器
 *
 * @author c
 * @date 2021/07/29
 */
@Api(tags = ["标签模块"])
@RestController
class TagController (
    private val tagService: TagService
        ){

    /**
     * 查询标签列表
     *
     * @return [<] 标签列表
     */
    @ApiOperation(value = "查询标签列表")
    @GetMapping("/tags")
    fun listTags(): Result<PageResult<TagDTO>> {
        return Result.ok(tagService.listTags())
    }

    /**
     * 查询后台标签列表
     *
     * @param condition 条件
     * @return [<] 标签列表
     */
    @ApiOperation(value = "查询后台标签列表")
    @GetMapping("/admin/tags")
    fun listTagBackDTO(condition: ConditionVO?): Result<PageResult<TagBackDTO>> {
        return Result.ok(tagService.listTagBackDTO(condition))
    }

    /**
     * 搜索文章标签
     *
     * @param condition 条件
     * @return [<] 标签列表
     */
    @ApiOperation(value = "搜索文章标签")
    @GetMapping("/admin/tags/search")
    fun listTagsBySearch(condition: ConditionVO?): Result<List<TagDTO>> {
        return Result.ok(tagService.listTagsBySearch(condition))
    }

    /**
     * 添加或修改标签
     *
     * @param tagVO 标签信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/admin/tags")
    fun saveOrUpdateTag(@RequestBody tagVO: @Valid TagVO?): Result<*> {
        tagService.saveOrUpdateTag(tagVO)
        return Result.ok<Any>()
    }

    /**
     * 删除标签
     *
     * @param tagIdList 标签id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    fun deleteTag(@RequestBody tagIdList: List<Int>?): Result<*> {
        tagService.deleteTag(tagIdList)
        return Result.ok<Any>()
    }
}
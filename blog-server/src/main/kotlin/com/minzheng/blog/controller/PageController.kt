package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.service.PageService
import com.minzheng.blog.vo.PageVO
import com.minzheng.blog.vo.Result
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * 页面控制器
 *
 * @author c
 * @date 2021/08/09
 */
@Api(tags = ["页面模块"])
@RestController
class PageController(
    private val pageService: PageService
) {

    /**
     * 删除页面
     *
     * @param pageId 页面id
     * @return [&lt;&gt;][Result]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除页面")
    @ApiImplicitParam(name = "pageId", value = "页面id", required = true, dataType = "Integer")
    @DeleteMapping("/admin/pages/{pageId}")
    fun deletePage(@PathVariable("pageId") pageId: Int?): Result<*> {
        pageService.deletePage(pageId)
        return Result.ok<Any>()
    }

    /**
     * 保存或更新页面
     *
     * @param pageVO 页面信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新页面")
    @PostMapping("/admin/pages")
    fun saveOrUpdatePage(@RequestBody pageVO: @Valid PageVO?): Result<*> {
        pageService.saveOrUpdatePage(pageVO)
        return Result.ok<Any>()
    }

    /**
     * 获取页面列表
     *
     * @return [<]
     */
    @ApiOperation(value = "获取页面列表")
    @GetMapping("/admin/pages")
    fun listPages(): Result<List<PageVO>> {
        return Result.ok(pageService.listPages())
    }
}
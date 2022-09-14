package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.dto.*
import com.minzheng.blog.service.CategoryService
import com.minzheng.blog.vo.CategoryVO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.Result
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * 分类控制器
 *
 * @author c
 * @date 2021/07/29
 */
@Api(tags = ["分类模块"])
@RestController
class CategoryController(
    private val categoryService: CategoryService
) {

    /**
     * 查看分类列表
     *
     * @return [<] 分类列表
     */
    @ApiOperation(value = "查看分类列表")
    @GetMapping("/categories")
    fun listCategories(): Result<PageResult<CategoryDTO>> {
        return Result.ok(categoryService.listCategories())
    }

    /**
     * 查看后台分类列表
     *
     * @param condition 条件
     * @return [<] 后台分类列表
     */
    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/admin/categories")
    fun listBackCategories(condition: ConditionVO): Result<PageResult<CategoryBackDTO>> {
        return Result.ok(categoryService.listBackCategories(condition))
    }

    /**
     * 搜索文章分类
     *
     * @param condition 条件
     * @return [<] 分类列表
     */
    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/admin/categories/search")
    fun listCategoriesBySearch(condition: ConditionVO): Result<List<CategoryOptionDTO>> {
        return Result.ok(categoryService.listCategoriesBySearch(condition))
    }

    /**
     * 添加或修改分类
     *
     * @param categoryVO 分类信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改分类")
    @PostMapping("/admin/categories")
    fun saveOrUpdateCategory(@RequestBody categoryVO: @Valid CategoryVO): Result<*> {
        categoryService.saveOrUpdateCategory(categoryVO)
        return Result.ok<Any>()
    }

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/categories")
    fun deleteCategories(@RequestBody categoryIdList: List<Int>): Result<*> {
        categoryService.deleteCategory(categoryIdList)
        return Result.ok<Any>()
    }

    @ApiOperation(value = "查看分类列表")
    @GetMapping("/admin/categories/tree")
    fun listCategoriesTree(): Result<List<CategoryMindTreeVo>?>? {
        var listCategoriesMindTree = categoryService.listCategoriesMindTree()
        return Result.ok(listCategoriesMindTree)
    }
}
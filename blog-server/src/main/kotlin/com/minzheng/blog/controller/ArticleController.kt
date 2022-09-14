package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.dto.*
import com.minzheng.blog.enums.FilePathEnum
import com.minzheng.blog.service.ArticleService
import com.minzheng.blog.strategy.context.UploadStrategyContext
import com.minzheng.blog.vo.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

/**
 * 文章控制器
 *
 * @author c
 */
@Api(tags = ["文章模块"])
@RestController
class ArticleController(
    private val articleService: ArticleService,
    private val uploadStrategyContext: UploadStrategyContext
) {

    /**
     * 查看文章归档
     *
     * @return [<] 文章归档列表
     */
    @ApiOperation(value = "查看文章归档")
    @GetMapping("/articles/archives")
    fun listArchives(): Result<PageResult<ArchiveDTO>> {
        return Result.ok(articleService.listArchives())
    }

    /**
     * 查看首页文章
     *
     * @return [<] 首页文章列表
     */
    @ApiOperation(value = "查看首页文章")
    @GetMapping("/articles")
    fun listArticles(): Result<List<ArticleHomeDTO>> {
        return Result.ok(articleService.listArticles())
    }

    /**
     * 分类页面 文章列表
     */
    @ApiOperation(value = "分类页面 文章列表")
    @GetMapping("/articles/con")
    fun listArticles(conditionVO: ConditionVO?): Result<List<ArticleHomeDTO>> {
        return Result.ok(articleService.pageArticlesWithCondition(conditionVO))
    }

    /**
     * 查看后台文章
     *
     * @param conditionVO 条件
     * @return [<] 后台文章列表
     */
    @ApiOperation(value = "查看后台文章")
    @GetMapping("/admin/articles")
    fun listArticleBacks(conditionVO: ConditionVO): Result<PageResult<ArticleBackDTO>> {
        return Result.ok(articleService.listArticleBacks(conditionVO))
    }

    /**
     * 添加或修改文章
     *
     * @param articleVO 文章信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/admin/articles")
    fun saveOrUpdateArticle(@RequestBody articleVO: @Valid ArticleVO): Result<*> {
        articleService.saveOrUpdateArticle(articleVO)
        return Result.ok<Any>()
    }

    /**
     * 修改文章置顶状态
     *
     * @param articleTopVO 文章置顶信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改文章置顶")
    @PutMapping("/admin/articles/top")
    fun updateArticleTop(@RequestBody articleTopVO: @Valid ArticleTopVO): Result<*> {
        articleService.updateArticleTop(articleTopVO)
        return Result.ok<Any>()
    }

    /**
     * 恢复或删除文章
     *
     * @param deleteVO 逻辑删除信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "恢复或删除文章")
    @PutMapping("/admin/articles")
    fun updateArticleDelete(@RequestBody deleteVO: @Valid DeleteVO): Result<*> {
        articleService.updateArticleDelete(deleteVO)
        return Result.ok<Any>()
    }

    /**
     * 上传文章图片
     *
     * @param file 文件
     * @return [<] 文章图片地址
     */
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/articles/images")
    fun saveArticleImages(file: MultipartFile): Result<String> {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.path))
    }

    /**
     * 删除文章
     *
     * @param articleIdList 文章id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles")
    fun deleteArticles(@RequestBody articleIdList: List<Int>): Result<*> {
        articleService.deleteArticles(articleIdList)
        return Result.ok<Any>()
    }

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return [<] 后台文章
     */
    @ApiOperation(value = "根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/admin/articles/{articleId}")
    fun getArticleBackById(@PathVariable("articleId") articleId: Int): Result<ArticleVO> {
        return Result.ok(articleService.getArticleBackById(articleId))
    }

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return [<] 文章信息
     */
    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/articles/{articleId}")
    fun getArticleById(@PathVariable("articleId") articleId: Int): Result<ArticleDTO> {
        return Result.ok(articleService.getArticleById(articleId))
    }

    /**
     * 根据条件查询文章
     *
     * @param condition 条件
     * @return [<] 文章列表
     */
    @ApiOperation(value = "根据条件查询文章")
    @GetMapping("/articles/condition")
    fun listArticlesByCondition(condition: ConditionVO): Result<ArticlePreviewListDTO> {
        return Result.ok(articleService.listArticlesByCondition(condition))
    }

    /**
     * 搜索文章
     *
     * @param condition 条件
     * @return [<] 文章列表
     */
    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    fun listArticlesBySearch(condition: ConditionVO): Result<List<ArticleSearchDTO>> {
        return Result.ok(articleService.listArticlesBySearch(condition))
    }

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     * @return [<]
     */
    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @PostMapping("/articles/{articleId}/like")
    fun saveArticleLike(@PathVariable("articleId") articleId: Int): Result<*> {
        articleService.saveArticleLike(articleId)
        return Result.ok<Any>()
    }
}
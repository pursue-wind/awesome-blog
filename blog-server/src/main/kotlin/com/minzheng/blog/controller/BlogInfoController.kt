package com.minzheng.blog.controller

import com.minzheng.blog.service.BlogInfoService
import com.minzheng.blog.strategy.context.UploadStrategyContext
import io.swagger.annotations.ApiOperation
import com.minzheng.blog.dto.BlogHomeInfoDTO
import com.minzheng.blog.dto.BlogBackInfoDTO
import io.swagger.annotations.ApiImplicitParam
import org.springframework.web.multipart.MultipartFile
import com.minzheng.blog.enums.FilePathEnum
import javax.validation.Valid
import com.minzheng.blog.vo.WebsiteConfigVO
import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.service.WebSocketService
import com.minzheng.blog.vo.BlogInfoVO
import com.minzheng.blog.vo.Result
import com.minzheng.blog.vo.VoiceVO
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*

/**
 * 博客信息控制器
 *
 * @author c
 * @date 2021/07/28
 */
@Api(tags = ["博客信息模块"])
@RestController
class BlogInfoController(
    private val blogInfoService: BlogInfoService,
    private val webSocketService: WebSocketService,
    private val uploadStrategyContext: UploadStrategyContext
) {

    /**
     * 查看博客信息
     *
     * @return [<] 博客信息
     */
    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    fun fetchBlogHomeInfo(): Result<BlogHomeInfoDTO> {
        return Result.ok(blogInfoService.getBlogHomeInfo())
    }

    /**
     * 查看后台信息
     *
     * @return [<] 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    fun fetchBlogBackInfo(): Result<BlogBackInfoDTO> {
        return Result.ok(blogInfoService.getBlogBackInfo())
    }

    /**
     * 上传博客配置图片
     *
     * @param file 文件
     * @return [<] 博客配置图片
     */
    @ApiOperation(value = "上传博客配置图片")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/config/images")
    fun savePhotoAlbumCover(file: MultipartFile?): Result<String> {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.path))
    }

    /**
     * 更新网站配置
     *
     * @param websiteConfigVO 网站配置信息
     * @return [Result]
     */
    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/website/config")
    fun updateWebsiteConfig(@RequestBody websiteConfigVO: @Valid WebsiteConfigVO): Result<*> {
        blogInfoService.updateWebsiteConfig(websiteConfigVO)
        return Result.ok<Any>()
    }

    /**
     * 获取网站配置
     *
     * @return [<] 网站配置
     */
    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    fun fetchWebsiteConfig(): Result<WebsiteConfigVO> {
        return Result.ok(blogInfoService.getWebsiteConfig())
    }

    /**
     * 查看关于我信息
     *
     * @return [<] 关于我信息
     */
    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    fun fetchAbout(): Result<String> {
        return Result.ok(blogInfoService.getAbout())
    }

    /**
     * 修改关于我信息
     *
     * @param blogInfoVO 博客信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/admin/about")
    fun updateAbout(@RequestBody blogInfoVO: @Valid BlogInfoVO): Result<*> {
        blogInfoService.updateAbout(blogInfoVO)
        return Result.ok<Any>()
    }

    /**
     * 保存语音信息
     *
     * @param voiceVO 语音信息
     * @return [<] 语音地址
     */
    @ApiOperation(value = "上传语音")
    @PostMapping("/voice")
    fun sendVoice(voiceVO: VoiceVO?): Result<String> {
        webSocketService.sendVoice(voiceVO)
        return Result.ok()
    }

    /**
     * 上传访客信息
     *
     * @return [Result]
     */
    @PostMapping("/report")
    fun report(): Result<*> {
        blogInfoService.report()
        return Result.ok<Any>()
    }
}
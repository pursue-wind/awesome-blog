package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.dto.PhotoAlbumBackDTO
import com.minzheng.blog.dto.PhotoAlbumDTO
import com.minzheng.blog.enums.FilePathEnum
import com.minzheng.blog.service.PhotoAlbumService
import com.minzheng.blog.strategy.context.UploadStrategyContext
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.PhotoAlbumVO
import com.minzheng.blog.vo.Result
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

/**
 * 相册控制器
 *
 * @author c
 * @date 2021/08/04
 */
@Api(tags = ["相册模块"])
@RestController
class PhotoAlbumController(
    private val uploadStrategyContext: UploadStrategyContext,
    private val photoAlbumService: PhotoAlbumService
) {

    /**
     * 上传相册封面
     *
     * @param file 文件
     * @return [<] 相册封面地址
     */
    @ApiOperation(value = "上传相册封面")
    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/photos/albums/cover")
    fun savePhotoAlbumCover(file: MultipartFile): Result<String> {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.PHOTO.path))
    }

    /**
     * 保存或更新相册
     *
     * @param photoAlbumVO 相册信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新相册")
    @PostMapping("/admin/photos/albums")
    fun saveOrUpdatePhotoAlbum(@RequestBody photoAlbumVO: @Valid PhotoAlbumVO): Result<*> {
        photoAlbumService.saveOrUpdatePhotoAlbum(photoAlbumVO)
        return Result.ok<Any>()
    }

    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return [<] 相册列表
     */
    @ApiOperation(value = "查看后台相册列表")
    @GetMapping("/admin/photos/albums")
    fun listPhotoAlbumBacks(condition: ConditionVO): Result<PageResult<PhotoAlbumBackDTO>> {
        return Result.ok(photoAlbumService.listPhotoAlbumBacks(condition))
    }

    /**
     * 获取后台相册列表信息
     *
     * @return [<] 相册列表信息
     */
    @ApiOperation(value = "获取后台相册列表信息")
    @GetMapping("/admin/photos/albums/info")
    fun listPhotoAlbumBackInfos(): Result<List<PhotoAlbumDTO>> {
        return Result.ok(photoAlbumService.listPhotoAlbumBackInfos())
    }

    /**
     * 根据id获取后台相册信息
     *
     * @param albumId 相册id
     * @return [Result]相册信息
     */
    @ApiOperation(value = "根据id获取后台相册信息")
    @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataType = "Integer")
    @GetMapping("/admin/photos/albums/{albumId}/info")
    fun getPhotoAlbumBackById(@PathVariable("albumId") albumId: Int): Result<PhotoAlbumBackDTO> {
        return Result.ok(photoAlbumService.getPhotoAlbumBackById(albumId))
    }

    /**
     * 根据id删除相册
     *
     * @param albumId 相册id
     * @return [Result]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "根据id删除相册")
    @ApiImplicitParam(name = "albumId", value = "相册id", required = true, dataType = "Integer")
    @DeleteMapping("/admin/photos/albums/{albumId}")
    fun deletePhotoAlbumById(@PathVariable("albumId") albumId: Int): Result<*> {
        photoAlbumService.deletePhotoAlbumById(albumId)
        return Result.ok<Any>()
    }

    /**
     * 获取相册列表
     *
     * @return [<] 相册列表
     */
    @ApiOperation(value = "获取相册列表")
    @GetMapping("/photos/albums")
    fun listPhotoAlbums(): Result<List<PhotoAlbumDTO>> {
        return Result.ok(photoAlbumService.listPhotoAlbums())
    }
}
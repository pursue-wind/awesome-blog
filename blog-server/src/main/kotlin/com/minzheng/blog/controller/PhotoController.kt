package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.dto.PhotoBackDTO
import com.minzheng.blog.dto.PhotoDTO
import com.minzheng.blog.service.PhotoService
import com.minzheng.blog.vo.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * 照片控制器
 *
 * @author c
 * @date 2021/08/05
 */
@Api(tags = ["照片模块"])
@RestController
class PhotoController(
    private val photoService: PhotoService
) {

    /**
     * 获取后台照片列表
     *
     * @param condition 条件
     * @return [<] 照片列表
     */
    @ApiOperation(value = "根据相册id获取照片列表")
    @GetMapping("/admin/photos")
    fun listPhotos(condition: ConditionVO): Result<PageResult<PhotoBackDTO>> {
        return Result.ok(photoService.listPhotos(condition))
    }

    /**
     * 更新照片信息
     *
     * @param photoInfoVO 照片信息
     * @return [Result]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "更新照片信息")
    @PutMapping("/admin/photos")
    fun updatePhoto(@RequestBody photoInfoVO: @Valid PhotoInfoVO): Result<*> {
        photoService.updatePhoto(photoInfoVO)
        return Result.ok<Any>()
    }

    /**
     * 保存照片
     *
     * @param photoVO 照片
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE)
    @ApiOperation(value = "保存照片")
    @PostMapping("/admin/photos")
    fun savePhotos(@RequestBody photoVO: @Valid PhotoVO): Result<*> {
        photoService.savePhotos(photoVO)
        return Result.ok<Any>()
    }

    /**
     * 移动照片相册
     *
     * @param photoVO 照片信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "移动照片相册")
    @PutMapping("/admin/photos/album")
    fun updatePhotosAlbum(@RequestBody photoVO: @Valid PhotoVO): Result<*> {
        photoService.updatePhotosAlbum(photoVO)
        return Result.ok<Any>()
    }

    /**
     * 更新照片删除状态
     *
     * @param deleteVO 删除信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "更新照片删除状态")
    @PutMapping("/admin/photos/delete")
    fun updatePhotoDelete(@RequestBody deleteVO: @Valid DeleteVO): Result<*> {
        photoService.updatePhotoDelete(deleteVO)
        return Result.ok<Any>()
    }

    /**
     * 删除照片
     *
     * @param photoIdList 照片id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除照片")
    @DeleteMapping("/admin/photos")
    fun deletePhotos(@RequestBody photoIdList: List<Int>): Result<*> {
        photoService.deletePhotos(photoIdList)
        return Result.ok<Any>()
    }

    /**
     * 根据相册id查看照片列表
     *
     * @param albumId 相册id
     * @return [<] 照片列表
     */
    @ApiOperation(value = "根据相册id查看照片列表")
    @GetMapping("/albums/{albumId}/photos")
    fun listPhotosByAlbumId(@PathVariable("albumId") albumId: Int): Result<PhotoDTO> {
        return Result.ok(photoService.listPhotosByAlbumId(albumId))
    }


    @GetMapping("/photos/api")
    fun baiduApiGetPic(@RequestParam("q") q: String, @RequestParam("pn") pn: Int) =
        Result.ok(photoService.baiduApiGetPic(q, pn))

}
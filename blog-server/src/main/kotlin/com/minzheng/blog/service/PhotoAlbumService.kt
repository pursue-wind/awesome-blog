package com.minzheng.blog.service

import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.constant.CommonConst.FALSE
import com.minzheng.blog.constant.CommonConst.TRUE
import com.minzheng.blog.dao.PhotoAlbumDao
import com.minzheng.blog.dao.PhotoDao
import com.minzheng.blog.dto.PhotoAlbumBackDTO
import com.minzheng.blog.dto.PhotoAlbumDTO
import com.minzheng.blog.entity.Photo
import com.minzheng.blog.entity.PhotoAlbum
import com.minzheng.blog.enums.PhotoAlbumStatusEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.util.PageUtils
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.PhotoAlbumVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 相册服务
 *
 * @author c
 * @date 2021/08/04
 */
@Service
class PhotoAlbumService(
    private val photoAlbumDao: PhotoAlbumDao,
    private val photoDao: PhotoDao
) : ServiceImpl<PhotoAlbumDao, PhotoAlbum>() {

    @Transactional(rollbackFor = [Exception::class])
    fun saveOrUpdatePhotoAlbum(photoAlbumVO: PhotoAlbumVO) {
        // 查询相册名是否存在
        val album = photoAlbumDao.selectOne(
            KtQueryWrapper(PhotoAlbum())
                .select(PhotoAlbum::id)
                .eq(PhotoAlbum::albumName, photoAlbumVO.albumName)
        )
        if (Objects.nonNull(album) && album.id != photoAlbumVO.id) {
            throw BizException("相册名已存在")
        }
        val photoAlbum = BeanCopyUtils.copyObject(photoAlbumVO, PhotoAlbum::class.java)
        this.saveOrUpdate(photoAlbum)
    }

    fun listPhotoAlbumBacks(condition: ConditionVO): PageResult<PhotoAlbumBackDTO> {
        // 查询相册数量
        val count = photoAlbumDao.selectCount(
            KtQueryWrapper(PhotoAlbum())
                .like(StringUtils.isNotBlank(condition.keywords), PhotoAlbum::albumName, condition.keywords)
                .eq(PhotoAlbum::isDelete, FALSE)
        )
        if (count == 0L) {
            return PageResult()
        }
        // 查询相册信息
        val photoAlbumBackList =
            photoAlbumDao.listPhotoAlbumBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition)
        return PageResult(photoAlbumBackList, count)
    }

    fun listPhotoAlbumBackInfos(): List<PhotoAlbumDTO> {
        val photoAlbumList = photoAlbumDao.selectList(
            KtQueryWrapper(PhotoAlbum())
                .eq(PhotoAlbum::isDelete, FALSE)
        )
        return BeanCopyUtils.copyList(photoAlbumList, PhotoAlbumDTO::class.java)
    }

    fun getPhotoAlbumBackById(albumId: Int?): PhotoAlbumBackDTO {
        // 查询相册信息
        val photoAlbum = photoAlbumDao.selectById(albumId)
        // 查询照片数量
        val photoCount = photoDao.selectCount(
            KtQueryWrapper(Photo())
                .eq(Photo::albumId, albumId)
                .eq(Photo::isDelete, FALSE)
        )
        val album = BeanCopyUtils.copyObject(photoAlbum, PhotoAlbumBackDTO::class.java)
        album.photoCount = photoCount
        return album
    }

    fun deletePhotoAlbumById(albumId: Int?) {
        // 查询照片数量
        val count = photoDao.selectCount(
            KtQueryWrapper(Photo())
                .eq(Photo::albumId, albumId)
        )
        if (count > 0) {
            // 若相册下存在照片则逻辑删除相册和照片
            photoAlbumDao.updateById(
                PhotoAlbum().apply {
                    id = albumId
                    isDelete = TRUE
                }
            )
            photoDao.update(
                Photo(), KtUpdateWrapper(Photo())
                    .set(Photo::isDelete, TRUE)
                    .eq(Photo::albumId, albumId)
            )
        } else {
            // 若相册下不存在照片则直接删除
            photoAlbumDao.deleteById(albumId)
        }
    }

    fun listPhotoAlbums(): List<PhotoAlbumDTO> {
        // 查询相册列表
        val photoAlbumList = photoAlbumDao.selectList(
            KtQueryWrapper(PhotoAlbum())
                .eq(PhotoAlbum::status, PhotoAlbumStatusEnum.PUBLIC.status)
                .eq(PhotoAlbum::isDelete, FALSE)
                .orderByDesc(PhotoAlbum::id)
        )
        return BeanCopyUtils.copyList(photoAlbumList, PhotoAlbumDTO::class.java)
    }
}
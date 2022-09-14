package com.minzheng.blog.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.baomidou.mybatisplus.core.toolkit.IdWorker
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.constant.CommonConst.FALSE
import com.minzheng.blog.dao.PhotoDao
import com.minzheng.blog.dto.PhotoBackDTO
import com.minzheng.blog.dto.PhotoDTO
import com.minzheng.blog.entity.Photo
import com.minzheng.blog.entity.PhotoAlbum
import com.minzheng.blog.enums.PhotoAlbumStatusEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.util.PageUtils
import com.minzheng.blog.vo.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URLEncoder
import java.util.*

/**
 * 照片服务
 *
 * @author c
 */
@Service
class PhotoService(
    private val photoDao: PhotoDao,
    private val photoAlbumService: PhotoAlbumService
) : ServiceImpl<PhotoDao, Photo>() {

    fun listPhotos(condition: ConditionVO): PageResult<PhotoBackDTO> {
        // 查询照片列表
        val page: Page<Photo> = Page<Photo>(PageUtils.getCurrent(), PageUtils.getSize())
        val photoPage: Page<Photo> = photoDao.selectPage(
            page, KtQueryWrapper(Photo())
                .eq(
                    condition.albumId != null,
                    Photo::albumId,
                    condition.albumId
                )
                .eq(Photo::isDelete, condition.isDelete)
                .orderByDesc(Photo::id)
                .orderByDesc(Photo::createTime)
        )
        val photoList: List<PhotoBackDTO> = BeanCopyUtils.copyList(photoPage.records, PhotoBackDTO::class.java)
        return PageResult<PhotoBackDTO>(photoList, photoPage.total)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updatePhoto(photoInfoVO: PhotoInfoVO?) {
        val photo: Photo = BeanCopyUtils.copyObject<Photo>(photoInfoVO, Photo::class.java)
        photoDao.updateById(photo)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun savePhotos(photoVO: PhotoVO) {
        val photoList: List<Photo> = photoVO.getPhotoUrlList().map {
            Photo().apply {
                albumId = photoVO.albumId
                photoName = IdWorker.getIdStr()
                photoSrc = it
            }
        }
        saveBatch(photoList)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updatePhotosAlbum(photoVO: PhotoVO) {
        val photoList: List<Photo> = photoVO.getPhotoIdList().map {
            Photo().apply {
                id = it
                albumId = photoVO.albumId
            }
        }
        this.updateBatchById(photoList)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updatePhotoDelete(deleteVO: DeleteVO) {
        // 更新照片状态
        val photoList: List<Photo> = deleteVO.idList.map {
            Photo().apply {
                id = it
                isDelete = deleteVO.isDelete
            }
        }
        this.updateBatchById(photoList)
        // 若恢复照片所在的相册已删除，恢复相册
        if (deleteVO.isDelete == FALSE) {
            val photoAlbumList: List<PhotoAlbum> = photoDao.selectList(
                KtQueryWrapper(Photo())
                    .select(Photo::albumId)
                    .`in`(Photo::id, deleteVO.getIdList())
                    .groupBy(Photo::albumId)
            ).map {
                PhotoAlbum().apply {
                    id = it.albumId
                    isDelete = FALSE
                }
            }
            photoAlbumService.updateBatchById(photoAlbumList)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deletePhotos(photoIdList: List<Int?>?) {
        photoDao.deleteBatchIds(photoIdList)
    }

    fun listPhotosByAlbumId(albumId: Int?): PhotoDTO {
        // 查询相册信息
        val photoAlbum: PhotoAlbum = photoAlbumService.getOne(
            KtQueryWrapper(PhotoAlbum())
                .eq(PhotoAlbum::id, albumId)
                .eq(PhotoAlbum::isDelete, FALSE)
                .eq(PhotoAlbum::status, PhotoAlbumStatusEnum.PUBLIC.status)
        )
        if (Objects.isNull(photoAlbum)) {
            throw BizException("相册不存在")
        }
        // 查询照片列表
        val page: Page<Photo> = Page<Photo>(PageUtils.getCurrent(), PageUtils.getSize())
        val photoList2 = photoDao.selectPage(
            page, KtQueryWrapper(Photo())
                .select(Photo::photoSrc)
                .eq(Photo::albumId, albumId)
                .eq(Photo::isDelete, FALSE)
                .orderByDesc(Photo::id)
        )
            .records
            .map { it.photoSrc }

        return PhotoDTO(
            photoAlbum.albumCover,
            photoAlbum.albumName,
            photoList2
        )
    }


    val headers: HttpRequestBuilder.() -> Unit = {
        header(
            "Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
        )
        header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
        header("Cache-Control", "max-age=0")
        header("Connection", "keep-alive")
        header(
            "Cookie",
            "MCITY=-%3A; BIDUPSID=85A59B3926497AC341ED333001C0D48F1; PSTM=1634737197; ; BAIDUID=F2EAAC30A8B3C8D76D7F02B13B2CD13A:FG=1; BAIDUID_BFESS=F2EAAC30A8B3C8D76D7F02B13B2CD13A:FG=1; BDRCVFR[-pGxjrCMryR]=mk3SLVN4HKm; BIDUPSID=F2EAAC30A8B3C8D7A58626DDFCE3C1C5"
        )
        header("Sec-Fetch-Dest", "document")
        header("Sec-Fetch-Mode", "navigate")
        header("Sec-Fetch-Site", "none")
        header("Sec-Fetch-User", "?1")
        header("Upgrade-Insecure-Requests", "1")
        header(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.5112.81 Safari/537.36 Edg/104.0.1293.54"
        )
        header("sec-ch-ua", "\"Chromium\";v=\"104\", \" Not A;Brand\";v=\"99\", \"Microsoft Edge\";v=\"104\"")
        header("sec-ch-ua-mobile", "?0")
        header("sec-ch-ua-platform", "\"Windows\"")
    }
    val client = HttpClient()

    fun baiduApiGetPic(q: String, pn: Int): List<String> {
        val res = runBlocking {
            val encodeQ = URLEncoder.encode(q, "utf-8")
            val response =
                client.get(
                    "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&word=$encodeQ&pn=$pn",
                    headers
                )
            println(response.bodyAsText())
            return@runBlocking response.bodyAsText()
        }

        val jsonObject = JSON.parse(res) as JSONObject
        val jsonArray = jsonObject["data"] as JSONArray
        return jsonArray
            .map { it as JSONObject }
            .filter { it["middleURL"] != null }
            .map { it["middleURL"] as String }
    }

}
package com.minzheng.blog.dto

/**
 * 照片dto
 *
 * @author c
 * @date 2021/08/05
 */
data class PhotoDTO(
    /**
     * 相册封面
     */
    val photoAlbumCover: String? = null,

    /**
     * 相册名
     */
    val photoAlbumName: String? = null,

    /**
     * 照片列表
     */
    val photoList: List<String?>
)
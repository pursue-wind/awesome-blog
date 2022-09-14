package com.minzheng.blog.entity

import com.baomidou.mybatisplus.annotation.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

/**
 * 相册
 *
 * @author c
 * @date 2021/08/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_photo_album")
class PhotoAlbum {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    var id: Int? = null

    /**
     * 相册名
     */
    var albumName: String? = null

    /**
     * 相册描述
     */
    var albumDesc: String? = null

    /**
     * 相册封面
     */
    var albumCover: String? = null

    /**
     * 是否删除
     */
    var isDelete: Int? = null

    /**
     * 状态值 1公开 2私密
     */
    var status: Int? = null

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    var createTime: LocalDateTime? = null

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    var updateTime: LocalDateTime? = null
}
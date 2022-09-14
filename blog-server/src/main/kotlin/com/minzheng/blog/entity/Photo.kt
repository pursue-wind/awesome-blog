package com.minzheng.blog.entity

import com.baomidou.mybatisplus.annotation.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

/**
 * 照片
 *
 * @author c
 * @date 2021/08/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_photo")
class Photo {
    /**
     * 照片id
     */
    @TableId(type = IdType.AUTO)
    var id: Int? = null

    /**
     * 相册id
     */
    var albumId: Int? = null

    /**
     * 照片名
     */
    var photoName: String? = null

    /**
     * 照片描述
     */
    var photoDesc: String? = null

    /**
     * 照片地址
     */
    var photoSrc: String? = null

    /**
     * 是否删除
     */
    var isDelete: Int? = null

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
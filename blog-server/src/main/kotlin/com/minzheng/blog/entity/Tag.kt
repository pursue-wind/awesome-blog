package com.minzheng.blog.entity

import com.baomidou.mybatisplus.annotation.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.Builder
import lombok.Data
import java.time.LocalDateTime

/**
 * 标签
 *
 * @author c
 * @date 2021/08/10
 * @since 2020-05-18
 */
@TableName("tb_tag")
class Tag {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Int? = null

    /**
     * 标签名
     */
    var tagName: String? = null

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
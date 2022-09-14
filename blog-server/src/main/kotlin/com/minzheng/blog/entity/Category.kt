package com.minzheng.blog.entity

import com.baomidou.mybatisplus.annotation.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

/**
 * 分类
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_category")
class Category {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Int? = null

    /**
     * 分类名
     */
    var categoryName: String? = null

    /**
     * 父分类
     */
    var parentId: Int? = null

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
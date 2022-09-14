package com.minzheng.blog.entity

import com.baomidou.mybatisplus.annotation.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import lombok.Builder
import lombok.Data
import java.time.LocalDateTime

/**
 * 文章
 *
 * @author c
 * @date 2021/07/29
 * @since 2020-05-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_article")
class Article {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    var id: Int? = null

    /**
     * 作者
     */
    var userId: Int? = null

    /**
     * 文章分类
     */
    var categoryId: Int? = null

    /**
     * 文章缩略图
     */
    var articleCover: String? = null

    /**
     * 标题
     */
    var articleTitle: String? = null

    /**
     * 内容
     */
    var articleContent: String? = null

    /**
     * 文章类型
     */
    var type: Int? = null

    /**
     * 原文链接
     */
    var originalUrl: String? = null

    /**
     * 是否置顶
     */
    var isTop: Int? = null

    /**
     * 是否删除
     */
    var isDelete: Int? = null

    /**
     * 文章状态 1.公开 2.私密 3.评论可见
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
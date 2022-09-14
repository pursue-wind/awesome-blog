package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.ArticleTag
import org.springframework.stereotype.Repository

/**
 * 文章标签
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface ArticleTagDao : BaseMapper<ArticleTag>
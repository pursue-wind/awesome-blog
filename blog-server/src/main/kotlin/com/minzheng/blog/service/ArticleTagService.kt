package com.minzheng.blog.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.dao.ArticleTagDao
import com.minzheng.blog.entity.ArticleTag
import org.springframework.stereotype.Service

/**
 * 文章标签服务
 *
 * @author c
 * @date 2021/08/10
 */
@Service
class ArticleTagService : ServiceImpl<ArticleTagDao, ArticleTag>()
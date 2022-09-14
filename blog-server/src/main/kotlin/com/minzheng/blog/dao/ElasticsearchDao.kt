package com.minzheng.blog.dao

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import com.minzheng.blog.dto.ArticleSearchDTO
import org.springframework.stereotype.Repository

/**
 * elasticsearch
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface ElasticsearchDao : ElasticsearchRepository<ArticleSearchDTO?, Int>
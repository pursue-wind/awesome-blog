package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.conditions.Wrapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.core.toolkit.Constants
import com.minzheng.blog.entity.Article
import com.minzheng.blog.dto.ArticleHomeDTO
import com.minzheng.blog.dto.ArticleDTO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.ArticlePreviewDTO
import com.minzheng.blog.dto.ArticleBackDTO
import com.minzheng.blog.dto.ArticleRecommendDTO
import com.minzheng.blog.dto.ArticleStatisticsDTO
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 文章
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface ArticleDao : BaseMapper<Article> {
    /**
     * 查询首页文章
     *
     * @param current 页码
     * @param size    大小
     * @return 文章列表
     */
    fun listArticles(@Param("current") current: Long, @Param("size") size: Long): List<ArticleHomeDTO>

    /**
     * 查询首页文章
     *
     * @param current 页码
     * @param size    大小
     * @return 文章列表
     */
    fun <T> pageArticlesWithCondition(page: IPage<ArticleHomeDTO>, @Param(Constants.WRAPPER) wrapper: Wrapper<T>): List<ArticleHomeDTO>

    /**
     * 根据id查询文章
     *
     * @param articleId 文章id
     * @return 文章信息
     */
    fun getArticleById(@Param("articleId") articleId: Int): ArticleDTO

    /**
     * 根据条件查询文章
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    fun listArticlesByCondition(
        @Param("current") current: Long,
        @Param("size") size: Long,
        @Param("condition") condition: ConditionVO
    ): List<ArticlePreviewDTO>

    /**
     * 查询后台文章
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    fun listArticleBacks(
        @Param("current") current: Long,
        @Param("size") size: Long,
        @Param("condition") condition: ConditionVO
    ): List<ArticleBackDTO>

    /**
     * 查询后台文章总量
     *
     * @param condition 条件
     * @return 文章总量
     */
    fun countArticleBacks(@Param("condition") condition: ConditionVO): Long

    /**
     * 查看文章的推荐文章
     *
     * @param articleId 文章id
     * @return 文章列表
     */
    fun listRecommendArticles(@Param("articleId") articleId: Int): List<ArticleRecommendDTO>

    /**
     * 文章统计
     *
     * @return [<] 文章统计结果
     */
    fun listArticleStatistics(): List<ArticleStatisticsDTO>
}
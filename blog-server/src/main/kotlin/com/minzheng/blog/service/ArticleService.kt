package com.minzheng.blog.service

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils
import com.baomidou.mybatisplus.core.toolkit.Wrappers
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.constant.CommonConst.ARTICLE_SET
import com.minzheng.blog.constant.CommonConst.FALSE
import com.minzheng.blog.constant.RedisPrefixConst
import com.minzheng.blog.dao.ArticleDao
import com.minzheng.blog.dao.ArticleTagDao
import com.minzheng.blog.dao.CategoryDao
import com.minzheng.blog.dao.TagDao
import com.minzheng.blog.dto.*
import com.minzheng.blog.entity.Article
import com.minzheng.blog.entity.ArticleTag
import com.minzheng.blog.entity.Category
import com.minzheng.blog.entity.Tag
import com.minzheng.blog.enums.ArticleStatusEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.notNull
import com.minzheng.blog.strategy.context.SearchStrategyContext
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.util.PageUtils
import com.minzheng.blog.util.UserUtils
import com.minzheng.blog.vo.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.servlet.http.HttpSession
import kotlin.collections.HashSet

/**
 * 文章服务
 *
 * @author c
 */
@Service
class ArticleService(
    private val articleDao: ArticleDao,
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao,
    private val tagService: TagService,
    private val articleTagDao: ArticleTagDao,
    private val searchStrategyContext: SearchStrategyContext,
    private val session: HttpSession,
    private val redisService: RedisService,
    private val articleTagService: ArticleTagService,
) : ServiceImpl<ArticleDao, Article>() {


    fun listArchives(): PageResult<ArchiveDTO> {
        val page = Page<Article>(PageUtils.getCurrent(), PageUtils.getSize())
        // 获取分页数据
        val articlePage = articleDao.selectPage(
            page,
            Wrappers.query<Article>().select("id", "article_title", "create_time").orderByDesc("create_time")
                .eq("is_delete", FALSE).eq("status", ArticleStatusEnum.PUBLIC.status)
        )
        val archiveDTOList = BeanCopyUtils.copyList(articlePage.records, ArchiveDTO::class.java)
        return PageResult(archiveDTOList, articlePage.total)
    }

    fun listArticleBacks(condition: ConditionVO): PageResult<ArticleBackDTO> {
        // 查询文章总量
        val count = articleDao.countArticleBacks(condition)
        if (count == 0L) {
            return PageResult()
        }
        // 查询后台文章
        val articleBackDTOList =
            articleDao.listArticleBacks(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition)
        // 查询文章点赞量和浏览量
        val viewsCountMap = redisService.zAllScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT)
        val likeCountMap = redisService.hGetAll(RedisPrefixConst.ARTICLE_LIKE_COUNT)
        // 封装点赞量和浏览量
        articleBackDTOList.forEach {
            val viewsCount = viewsCountMap?.get(it.id)
            if (Objects.nonNull(viewsCount)) {
                it.viewsCount = viewsCount?.toInt()
            }
            it.likeCount = likeCountMap?.get(it.id.toString()) as Int?
        }
        return PageResult(articleBackDTOList, count)
    }

    fun listArticles(): List<ArticleHomeDTO>? {
        return articleDao.listArticles(PageUtils.getLimitCurrent(), PageUtils.getSize())
    }

    fun pageArticlesWithCondition(condition: ConditionVO?): List<ArticleHomeDTO>? {
        return articleDao.pageArticlesWithCondition(
            Page.of(PageUtils.getLimitCurrent(), PageUtils.getSize()),
            KtQueryWrapper(Article()).eq(condition != null, Article::categoryId, condition?.categoryId)
        )
    }

    fun listArticlesByCondition(condition: ConditionVO): ArticlePreviewListDTO {
        // 查询文章
        var articlePreviews =
            articleDao.listArticlesByCondition(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition)
        // 搜索条件对应名(标签或分类名)

        var categoryName = condition.categoryId?.let {
            var category = categoryDao.selectOne(
                KtQueryWrapper(Category::class.java)
                    .select(Category::categoryName)
                    .eq(Category::id, condition.categoryId)
            )
            category?.categoryName
        }

        val tagName = condition.tagId?.let {
            var tag = tagDao.selectOne(
                KtQueryWrapper(Tag::class.java)
                    .select(Tag::tagName)
                    .eq(Tag::id, condition.tagId)
            )
            tag?.tagName
        }


        return ArticlePreviewListDTO().apply {
            articlePreviewDTOList = articlePreviews
            name = notNull(categoryName, tagName) { "" }
        }
    }


    fun getArticleById(articleId: Int): ArticleDTO {
        // 查询推荐文章
        val recommendArticleList = CompletableFuture.supplyAsync { articleDao.listRecommendArticles(articleId) }
        // 查询最新文章
        val newestArticleList = CompletableFuture.supplyAsync {
            val articleList = articleDao.selectList(
                Wrappers.query<Article>().select("id", "article_title", "article_cover", "create_time")
                    .orderByDesc("id").eq("is_delete", FALSE).eq("status", ArticleStatusEnum.PUBLIC.status)
                    .last("limit 5")
            )
            BeanCopyUtils.copyList(articleList, ArticleRecommendDTO::class.java)
        }
        // 查询id对应文章
        val article = articleDao.getArticleById(articleId)
        if (Objects.isNull(article)) {
            throw BizException("文章不存在")
        }
        // 更新文章浏览量
        updateArticleViewsCount(articleId)
        // 查询上一篇下一篇文章
        val lastArticle = articleDao.selectOne(
            Wrappers.query<Article>().select("id", "article_title", "article_cover").orderByDesc("id")
                .eq("is_delete", FALSE).eq("status", ArticleStatusEnum.PUBLIC.status).lt("id", articleId)
                .last("limit 1")
        )
        val nextArticle = articleDao.selectOne(
            Wrappers.query<Article>().select("id", "article_title", "article_cover").orderByDesc("id")
                .eq("is_delete", FALSE).eq("status", ArticleStatusEnum.PUBLIC.status).gt("id", articleId)
                .last("limit 1")
        )
        article.lastArticle = BeanCopyUtils.copyObject(lastArticle, ArticlePaginationDTO::class.java)
        article.nextArticle = BeanCopyUtils.copyObject(nextArticle, ArticlePaginationDTO::class.java)
        // 封装点赞量和浏览量
        val score = redisService.zScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT, articleId)
        if (Objects.nonNull(score)) {
            article.viewsCount = score?.toInt()
        }
        article.likeCount =
            redisService.hGet(RedisPrefixConst.ARTICLE_LIKE_COUNT, articleId.toString())?.let { it as Int }
        // 封装文章信息
        try {
            article.recommendArticleList = recommendArticleList.get()
            article.newestArticleList = newestArticleList.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return article
    }

    /**
     * 更新文章浏览量
     *
     * @param articleId 文章id
     */
    fun updateArticleViewsCount(articleId: Int) {
        // 判断是否第一次访问，增加浏览量
        val articleSet = session.getAttribute(ARTICLE_SET)?.let { it as MutableSet<Int> } ?: HashSet()
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId)
            session.setAttribute(ARTICLE_SET, articleSet)
            // 浏览量+1
            redisService.zIncr(RedisPrefixConst.ARTICLE_VIEWS_COUNT, articleId, 1.0)
        }
    }

    fun saveArticleLike(articleId: Int) {
        // 判断是否点赞
        val articleLikeKey = RedisPrefixConst.ARTICLE_USER_LIKE + UserUtils.getLoginUser().userInfoId
        if (redisService.sIsMember(articleLikeKey, articleId) == true) {
            // 点过赞则删除文章id
            redisService.sRemove(articleLikeKey, articleId)
            // 文章点赞量-1
            redisService.hDecr(RedisPrefixConst.ARTICLE_LIKE_COUNT, articleId.toString(), 1L)
        } else {
            // 未点赞则增加文章id
            redisService.sAdd(articleLikeKey, articleId)
            // 文章点赞量+1
            redisService.hIncr(RedisPrefixConst.ARTICLE_LIKE_COUNT, articleId.toString(), 1L)
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    fun saveOrUpdateArticle(articleVO: ArticleVO) {
        // 保存文章分类
        val category = saveArticleCategory(articleVO)
        // 保存或修改文章
        val article = BeanCopyUtils.copyObject(articleVO, Article::class.java)
        if (Objects.nonNull(category)) {
            article.categoryId = category.id
        }
        article.userId = UserUtils.getLoginUser().userInfoId
        this.saveOrUpdate(article)
        // 保存文章标签
        saveArticleTag(articleVO, article.id!!)
    }

    /**
     * 保存文章分类
     *
     * @param articleVO 文章信息
     * @return [Category] 文章分类
     */
    private fun saveArticleCategory(articleVO: ArticleVO): Category {
        // 判断分类是否存在
        var category = categoryDao.selectOne(
            Wrappers.query<Category>().eq("category_name", articleVO.categoryName)
        )
        if (Objects.isNull(category) && articleVO.status != ArticleStatusEnum.DRAFT.status) {
            category = Category().apply { categoryName = articleVO.categoryName }
            categoryDao.insert(category)
        }
        return category
    }

    /**
     * 保存文章标签
     *
     * @param articleVO 文章信息
     */
    private fun saveArticleTag(articleVO: ArticleVO, articleId2: Int) {
        // 编辑文章则删除文章所有标签
        if (Objects.nonNull(articleVO.id)) {
            articleTagDao.delete(
                Wrappers.query<ArticleTag>().eq("article_id", articleVO.id)
            )
        }
        // 添加文章标签
        val tagNameList = articleVO.tagNameList
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            val existTagList = tagService.list(
                Wrappers.query<Tag>().`in`("tag_name", tagNameList)
            )
            val existTagNameList = existTagList.map { obj: Tag -> obj.tagName }

            val existTagIdList = existTagList.map { obj: Tag -> obj.id }.toMutableList()

            // 对比新增不存在的标签
            tagNameList.removeAll(existTagNameList)
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                val tagList = tagNameList.map { Tag().apply { tagName = it } }

                tagService.saveBatch(tagList)
                val tagIdList = tagList.map { it.id }
                existTagIdList.addAll(tagIdList)
            }
            // 提取标签id绑定文章
            val articleTagList = existTagIdList.map {
                ArticleTag().apply {
                    articleId = articleId2
                    tagId = it
                }
            }

            articleTagService.saveBatch(articleTagList)
        }
    }

    fun updateArticleTop(articleTopVO: ArticleTopVO) {
        // 修改文章置顶状态
        val article = Article().apply {
            id = articleTopVO.id
            isTop = articleTopVO.isTop
        }
        articleDao.updateById(article)
    }

    fun updateArticleDelete(deleteVO: DeleteVO) {
        // 修改文章逻辑删除状态
        val articleList = deleteVO.idList.map {
            Article().apply {
                id = it
                isTop = FALSE
                isDelete = deleteVO.isDelete
            }
        }

        this.updateBatchById(articleList)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun deleteArticles(articleIdList: List<Int>) {
        // 删除文章标签关联
        articleTagDao.delete(
            Wrappers.query<ArticleTag>().`in`("article_id", articleIdList)
        )
        // 删除文章
        articleDao.deleteBatchIds(articleIdList)
    }

    fun listArticlesBySearch(condition: ConditionVO): List<ArticleSearchDTO> {
        return searchStrategyContext.executeSearchStrategy(condition.keywords)
    }

    fun getArticleBackById(articleId: Int): ArticleVO {
        // 查询文章信息
        val article = articleDao.selectById(articleId)
        // 查询文章分类
        val category = categoryDao.selectById(article.categoryId)
        var categoryName: String? = null
        if (Objects.nonNull(category)) {
            categoryName = category.categoryName
        }
        // 查询文章标签
        val tagNameList = tagDao.listTagNameByArticleId(articleId)
        // 封装数据
        val articleVO = BeanCopyUtils.copyObject(article, ArticleVO::class.java)
        articleVO.categoryName = categoryName
        articleVO.tagNameList = tagNameList
        return articleVO
    }
}
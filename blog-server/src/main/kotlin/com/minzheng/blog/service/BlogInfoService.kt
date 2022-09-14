package com.minzheng.blog.service

import com.alibaba.fastjson.JSON
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.minzheng.blog.constant.CommonConst.CITY
import com.minzheng.blog.constant.CommonConst.DEFAULT_CONFIG_ID
import com.minzheng.blog.constant.CommonConst.FALSE
import com.minzheng.blog.constant.CommonConst.PROVINCE
import com.minzheng.blog.constant.CommonConst.UNKNOWN
import com.minzheng.blog.constant.RedisPrefixConst
import com.minzheng.blog.dao.*
import com.minzheng.blog.dto.*
import com.minzheng.blog.entity.Article
import com.minzheng.blog.entity.WebsiteConfig
import com.minzheng.blog.enums.ArticleStatusEnum
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.util.IpUtils.getIpAddress
import com.minzheng.blog.util.IpUtils.getIpSource
import com.minzheng.blog.util.IpUtils.getUserAgent
import com.minzheng.blog.vo.BlogInfoVO
import com.minzheng.blog.vo.WebsiteConfigVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import java.util.*
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

/**
 * 博客信息服务
 *
 * @author xiaojie
 * @since 2020-05-18
 */
@Service
class BlogInfoService(
    private val userInfoDao: UserInfoDao,
    private val articleDao: ArticleDao,
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao,
    private val messageDao: MessageDao,
    private val uniqueViewService: UniqueViewService,
    private val redisService: RedisService,
    private val websiteConfigDao: WebsiteConfigDao,
    private val request: HttpServletRequest,
    private val pageService: PageService
) {

    fun getBlogHomeInfo(): BlogHomeInfoDTO {
        // 查询文章数量
        val articleCount2 = articleDao.selectCount(
            KtQueryWrapper(Article::class.java)
                .eq(Article::status, ArticleStatusEnum.PUBLIC.status)
                .eq(Article::isDelete, FALSE)
        );
        // 查询分类数量
        val categoryCount2 = categoryDao.selectCount(null)
        // 查询标签数量
        val tagCount2 = tagDao.selectCount(null)
        // 查询访问量
        val count2 = redisService.get(RedisPrefixConst.BLOG_VIEWS_COUNT)
        val viewsCount2 = Optional.ofNullable(count2).orElse(0).toString()
        // 查询网站配置
        val websiteConfig2 = getWebsiteConfig()
        // 查询页面图片
        val pageVOList2 = pageService.listPages()
        // 封装数据
        return BlogHomeInfoDTO().apply {
            articleCount = articleCount2
            categoryCount = categoryCount2
            tagCount = tagCount2
            viewsCount = viewsCount2
            websiteConfig = websiteConfig2
            pageList = pageVOList2
        }
    }

    fun getBlogBackInfo(): BlogBackInfoDTO {
        // 查询访问量
        val count2: Any? = redisService.get(RedisPrefixConst.BLOG_VIEWS_COUNT)
        val viewsCount2 = Optional.ofNullable(count2).orElse(0).toString().toInt()
        // 查询留言量
        val messageCount2: Int = messageDao.selectCount(null).toInt()
        // 查询用户量
        val userCount2: Int = userInfoDao.selectCount(null).toInt()
        // 查询文章量
        val articleCount2 = articleDao.selectCount(
            KtQueryWrapper(Article::class.java)
                .eq(Article::isDelete, FALSE)
        ).toInt()
        // 查询一周用户量
        val uniqueViewList2: List<UniqueViewDTO> = uniqueViewService.listUniqueViews()
        // 查询文章统计
        val articleStatisticsList2: List<ArticleStatisticsDTO> = articleDao.listArticleStatistics()
        // 查询分类数据
        val categoryDTOList2: List<CategoryDTO>? = categoryDao.listCategoryDTO()
        // 查询标签数据
        val tagDTOList2 = BeanCopyUtils.copyList(tagDao.selectList(null), TagDTO::class.java)
        // 查询redis访问量前五的文章
        val articleMap: MutableMap<Any, Double>? =
            redisService.zReverseRangeWithScore(RedisPrefixConst.ARTICLE_VIEWS_COUNT, 0, 4) as MutableMap<Any, Double>?
        val blogBackInfoDTO = BlogBackInfoDTO().apply {
            articleStatisticsList = articleStatisticsList2
            tagDTOList = tagDTOList2
            viewsCount = viewsCount2
            messageCount = messageCount2
            userCount = userCount2
            articleCount = articleCount2
            categoryDTOList = categoryDTOList2
            uniqueViewDTOList = uniqueViewList2

        }

        if (CollectionUtils.isNotEmpty(articleMap)) {
            // 查询文章排行
            val articleRankDTOList = listArticleRank(articleMap)
            blogBackInfoDTO.articleRankDTOList = articleRankDTOList
        }
        return blogBackInfoDTO
    }

    fun updateWebsiteConfig(websiteConfigVO: WebsiteConfigVO) {
        // 修改网站配置
        val websiteConfig = WebsiteConfig().apply {
            id = 1
            config = JSON.toJSONString(websiteConfigVO)
        }
        websiteConfigDao.updateById(websiteConfig)
        // 删除缓存
        redisService.del(RedisPrefixConst.WEBSITE_CONFIG)
    }

    fun getWebsiteConfig(): WebsiteConfigVO {
        val websiteConfigVO: WebsiteConfigVO
        // 获取缓存数据
        val websiteConfig: Any? = redisService.get(RedisPrefixConst.WEBSITE_CONFIG)
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(
                websiteConfig.toString(),
                WebsiteConfigVO::class.java
            )
        } else {
            // 从数据库中加载
            val config: String = websiteConfigDao.selectById(DEFAULT_CONFIG_ID).getConfig()
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO::class.java)
            redisService.set(RedisPrefixConst.WEBSITE_CONFIG, config)
        }
        return websiteConfigVO
    }

    fun getAbout(): String {
        return redisService.get(RedisPrefixConst.ABOUT)?.toString() ?: ""
    }

    fun updateAbout(blogInfoVO: BlogInfoVO) {
        redisService.set(RedisPrefixConst.ABOUT, blogInfoVO.aboutContent)
    }

    fun report() {
        // 获取ip
        val ipAddress = getIpAddress(request)
        // 获取访问设备
        val userAgent = getUserAgent(request)
        val browser = userAgent.browser
        val operatingSystem = userAgent.operatingSystem
        // 生成唯一用户标识
        val uuid = ipAddress + browser.getName() + operatingSystem.getName()
        val md5 = DigestUtils.md5DigestAsHex(uuid.toByteArray())
        // 判断是否访问
        if (redisService.sIsMember(RedisPrefixConst.UNIQUE_VISITOR, md5) == false) {
            // 统计游客地域分布
            var ipSource = getIpSource(ipAddress)
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource!!.substring(0, 2)
                    .replace(PROVINCE.toRegex(), "")
                    .replace(CITY.toRegex(), "")
                redisService.hIncr(RedisPrefixConst.VISITOR_AREA, ipSource, 1L)
            } else {
                redisService.hIncr(RedisPrefixConst.VISITOR_AREA, UNKNOWN, 1L)
            }
            // 访问量+1
            redisService.incr(RedisPrefixConst.BLOG_VIEWS_COUNT, 1)
            // 保存唯一标识
            redisService.sAdd(RedisPrefixConst.UNIQUE_VISITOR, md5)
        }
    }

    /**
     * 查询文章排行   new LambdaQueryWrapper<Article>()
     * .select(Article::getId, Article::getArticleTitle)
     * .in(Article::getId, articleIdList)
     *
     * @param articleMap 文章信息
     * @return [<] 文章排行
    </Article> */
    private fun listArticleRank(articleMap: Map<Any, Double>?): List<ArticleRankDTO> {
        articleMap?.let {
            return emptyList()
        }

        // 提取文章id
        val articleIdList = articleMap?.keys?.map { it as Int }

        // 查询文章信息
        val queryWrapper = KtQueryWrapper(Article())
            .select(Article::id, Article::articleTitle)
            .`in`(Article::id, articleIdList)
        return articleDao.selectList(queryWrapper)
            .map { article ->
                ArticleRankDTO().apply {
                    articleTitle = article.articleTitle
                    viewsCount = articleMap?.getOrDefault(article.id as Any, 0)?.toInt()
                }
            }
            .sortedBy { it.viewsCount }
            .reversed()
    }
}
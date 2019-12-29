package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.article.ArticleDto;
import cn.mirrorming.blog.domain.dto.article.ArticleListDto;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.domain.po.ArticleContent;
import cn.mirrorming.blog.domain.po.Tag;
import cn.mirrorming.blog.exception.ArticleException;
import cn.mirrorming.blog.mapper.auto.ArticleContentMapper;
import cn.mirrorming.blog.mapper.auto.ArticleMapper;
import cn.mirrorming.blog.mapper.auto.CategoryMapper;
import cn.mirrorming.blog.mapper.auto.UsersMapper;
import cn.mirrorming.blog.utils.JacksonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mireal
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "article")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleContentMapper articleContentMapper;
    private final UsersMapper usersMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 查询文章列表，无内容
     */
    public List<ArticleListDto> selectAllArticle(int cur, int size) {
        return articleMapper.selectPage(
                new Page<>(cur, size),
                new QueryWrapper<Article>()
                        .orderByDesc(Article.COL_CREATE_TIME)
                        //文章不是私有，也不是草稿
                        .and(i -> i.eq(Article.COL_IS_PRIVATE, false).eq(Article.COL_IS_DRAFT, false)))
                .getRecords()
                .parallelStream()
                .map(article -> {
                    try {
                        return ArticleListDto.builder()
                                .article(article)
                                .user(usersMapper.selectUserById(article.getUserId()))
                                .category(categoryMapper.selectById(article.getCategoryId()))
                                .tags(JacksonUtils.json2list((String) article.getTag(), Tag.class))
                                .build();
                    } catch (Exception e) {
                        log.info("标签json转换出错：{}", e.getMessage());
                        throw new ArticleException("标签json转换出错");
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 归档页面，文章列表按照年份和月份归档
     */
    @Cacheable(key = "#root.methodName")
    public HashMap<Integer, Map<Integer, List<Article>>> filedArticle() {
        HashMap<Integer, Map<Integer, List<Article>>> res = Maps.newHashMap();
        articleMapper.selectList(
                new QueryWrapper<Article>()
                        .orderByDesc(Article.COL_CREATE_TIME)
                        //文章不是私有，也不是草稿
                        .and(i -> i.eq(Article.COL_IS_PRIVATE, false).eq(Article.COL_IS_DRAFT, false)))
                .stream()
                //按年归档
                .collect(Collectors.groupingBy(article -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(article.getCreateTime());
                    return calendar.get(Calendar.YEAR);
                }))
                //按月归档
                .forEach((k, v) -> {
                    res.put(k, v.stream().collect(Collectors.groupingBy(article -> {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(article.getCreateTime());
                        return calendar.get(Calendar.MONTH);
                    })));
                });
        return res;
    }

    /**
     * 查询文章，有文章内容
     */
    @Cacheable(key = "'articleContent-'+#a0+'-'+#a1")
    @SneakyThrows(Exception.class)
    public ArticleDto selectArticleById(Integer id, String readPassword) {
        Article article = articleMapper.selectById(id);
        Optional.ofNullable(article).orElseThrow(() -> new ArticleException("文章不存在"));

        if (StringUtils.isNotBlank(article.getReadPassword()) &&
                !StringUtils.equals(article.getReadPassword(), readPassword)) {
            throw new ArticleException("文章阅读密码错误");
        }
        //文章内容
        ArticleContent articleContent = articleContentMapper.selectOne(
                new QueryWrapper<ArticleContent>().eq(ArticleContent.COL_ARTICLE_ID, article.getId()));

        return ArticleDto.builder()
                .article(article)
                .articleContent(articleContent)
                //文章用户信息
                .user(usersMapper.selectUserById(article.getUserId()))
                //文章分类信息
                .category(categoryMapper.selectById(article.getCategoryId()))
                //文章标签列表
                .tags(JacksonUtils.json2list((String) article.getTag(), Tag.class))
                .build();
    }

    /**
     * 获取热门文章
     */
    public IPage<Article> selectHotArticle() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .orderByDesc(Article.COL_CLICK)
                .and(i -> i.eq(Article.COL_IS_PRIVATE, 0).eq(Article.COL_IS_DRAFT, 0));
        return articleMapper.selectPage(new Page<>(1, 5), queryWrapper);
    }

    //todo 单篇文章字数

    /**
     * 查询所有文章的字数和
     */
    public String selectWordNumberSum() {
        return articleMapper.selectWordNumberSum();
    }
}


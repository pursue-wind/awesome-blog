package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.article.ArticleDTO;
import cn.mirrorming.blog.domain.dto.article.ArticleListDTO;
import cn.mirrorming.blog.domain.dto.base.PageDTO;
import cn.mirrorming.blog.domain.dto.user.UserDTO;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.domain.po.ArticleContent;
import cn.mirrorming.blog.domain.po.Category;
import cn.mirrorming.blog.exception.ArticleException;
import cn.mirrorming.blog.mapper.generate.ArticleContentMapper;
import cn.mirrorming.blog.mapper.generate.ArticleMapper;
import cn.mirrorming.blog.mapper.generate.CategoryMapper;
import cn.mirrorming.blog.mapper.generate.UsersMapper;
import cn.mirrorming.blog.utils.CheckUtils;
import cn.mirrorming.blog.utils.ExceptionUtils;
import cn.mirrorming.blog.utils.Filler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Mireal Chan
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
    @Cacheable(key = "'ArticleList:'+#cur+':'+#size+':'+#categoryId")
    public PageDTO<List<ArticleListDTO>> selectArticlePage(int cur, int size, Integer categoryId) throws ExecutionException, InterruptedException {
        IPage<Article> articlePage = articleMapper.selectPage(
                new Page<>(cur, size),
                Wrappers.<Article>lambdaQuery()
                        .orderByDesc(Article::getCreateTime)
                        //文章不是私有，也不是草稿
                        .and(i -> i.eq(Article::getIsPrivate, false)
                                .eq(Article::getIsDraft, false)
                                .eq(categoryId != 0, Article::getCategoryId, categoryId)));

        //查询文章列表
        CompletableFuture<List<ArticleListDTO>> articleListsFuture = CompletableFuture.supplyAsync(() -> articlePage.getRecords()
                .parallelStream()
                .map(article -> ArticleListDTO.builder()
                        .id(article.getId())
                        .article(article.setReadPassword(StringUtils.isBlank(article.getReadPassword()) ? "" : "密"))
                        .build())
                .collect(Collectors.toList()));

        //填充用户
        CompletableFuture<Void> fillUser = articleListsFuture.thenCompose(articleLists ->
                CompletableFuture.runAsync(() -> {
                    Filler.fill(() -> articleLists,
                            data -> data.getArticle().getUserId(),
                            ArticleListDTO::setUser,
                            userIds -> Filler.list2Map(usersMapper.selectUserByIds(userIds), UserDTO::getId));
                }));

        //填充目录
        CompletableFuture<Void> fillCategory = articleListsFuture.thenCompose(articleLists ->
                CompletableFuture.runAsync(() -> {
                    Filler.fill(() -> articleLists,
                            data -> data.getArticle().getCategoryId(),
                            ArticleListDTO::setCategory,
                            categoryIds -> Filler.list2Map(categoryMapper.selectBatchIds(categoryIds), Category::getId));
                }));

        //等待所有线程执行完毕
        CompletableFuture.allOf(articleListsFuture, fillUser, fillCategory).get();

        return PageDTO.succeed(articleListsFuture.get(), articlePage.getCurrent(), articlePage.getTotal());
    }


    /**
     * 归档页面，文章列表按照年份和月份归档
     * 现在文章太少，后期考虑限制年份来返回
     * .apply("year(create_time)=year(date_sub(now(),interval {0} year))", 1)));
     *
     * @return
     */
    @Cacheable(key = "#root.methodName")
    public Map<String, Map<String, List<Article>>> filedArticle() {
        TreeMap<String, Map<String, List<Article>>> res = new TreeMap<>(
                (y, x) -> Integer.parseInt(x.substring(0, x.length() - 1)) - Integer.parseInt(y.substring(0, y.length() - 1)));
        res.putAll(
                articleMapper.selectList(
                        new QueryWrapper<Article>()
                                .orderByDesc(Article.COL_CREATE_TIME)
                                //文章不是私有，也不是草稿
                                .and(i -> i.eq(Article.COL_IS_PRIVATE, false)
                                        .eq(Article.COL_IS_DRAFT, false)))
                        .stream()
                        .peek(article -> article.setReadPassword(StringUtils.isBlank(article.getReadPassword()) ? "" : "密"))
                        //按年归档
                        .collect(Collectors.groupingBy(article -> {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(article.getCreateTime());
                            return calendar.get(Calendar.YEAR) + "年";
                        }))
                        .entrySet()
                        .stream()
                        //按月归档
                        .collect(
                                Collectors.toMap(
                                        Map.Entry::getKey,
                                        val -> val.getValue().stream().collect(Collectors.groupingBy(article -> {
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(article.getCreateTime());
                                            return calendar.get(Calendar.MONTH) + "月";
                                        }, () -> new TreeMap<>((y, x) ->
                                                Integer.parseInt(x.substring(0, x.length() - 1)) -
                                                        Integer.parseInt(y.substring(0, y.length() - 1))), Collectors.toList())))
                        )
        );
        return res;
    }

    /**
     * 查询文章，有文章内容
     */
    @SneakyThrows(Exception.class)
    @Cacheable(key = "'ArticleContent:'+#a0+':'+#a1")
    public ArticleDTO selectArticleById(Integer id, String readPassword) {
        Article article = articleMapper.selectById(id);
        Optional.ofNullable(article).orElseThrow(() -> new ArticleException("文章不存在"));

        CheckUtils.mustTrue(
                StringUtils.isNotBlank(article.getReadPassword()) && !StringUtils.equals(article.getReadPassword(), readPassword),
                () -> ExceptionUtils.articleEx("文章阅读密码错误"));

        //文章内容
        ArticleContent articleContent = articleContentMapper.selectOne(
                Wrappers.<ArticleContent>query().eq(ArticleContent.COL_ARTICLE_ID, article.getId()));
        //文章密码修改再返回
        article.setReadPassword(StringUtils.isBlank(article.getReadPassword()) ? "" : "密");

        return ArticleDTO.builder()
                .article(article)
                .articleContent(articleContent)
                //文章用户信息
                .user(usersMapper.selectUserById(article.getUserId()))
                //文章分类信息
                .category(categoryMapper.selectById(article.getCategoryId()))
                .build();
    }

    /**
     * 获取热门文章
     */
    public IPage<Article> selectHotArticle() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>()
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
package cn.mirrorming.blog.service.admin;

import cn.mirrorming.blog.domain.dto.article.AddArticleDTO;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.domain.po.ArticleContent;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.mapper.generate.ArticleContentMapper;
import cn.mirrorming.blog.mapper.generate.ArticleMapper;
import cn.mirrorming.blog.mapper.generate.CategoryMapper;
import cn.mirrorming.blog.mapper.generate.UsersMapper;
import cn.mirrorming.blog.utils.Check;
import cn.mirrorming.blog.utils.ExceptionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static cn.mirrorming.blog.domain.enums.RespEnum.*;

/**
 * @author Mireal Chan
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleContentMapper articleContentMapper;
    private final UsersMapper usersMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 查询文章列表
     *
     * @return
     */
    public List<Article> selectAllArticle(Integer userId, Integer categoryId) {
        return articleMapper.selectList(
                new QueryWrapper<Article>()
                        .orderByDesc(Article.COL_CREATE_TIME)
                        .and(i -> i.eq(Article.COL_USER_ID, userId)
                                .eq(categoryId != 0, Article.COL_CATEGORY_ID, categoryId)));
    }


    /**
     * 归档页面，文章列表按照年份和月份归档
     * 现在文章太少，后期考虑限制年份来返回
     * .apply("year(create_time)=year(date_sub(now(),interval {0} year))", 1)));
     *
     * @return
     */
    public Map<String, Map<String, List<Article>>> filedArticle() {

        TreeMap<String, Map<String, List<Article>>> res = new TreeMap<>(
                (y, x) -> Integer.parseInt(x.substring(0, x.length() - 1)) - Integer.parseInt(y.substring(0, y.length() - 1)));

        List<Article> articles = articleMapper.selectList(
                new QueryWrapper<Article>()
                        .orderByDesc(Article.COL_CREATE_TIME)
                        //文章不是私有，也不是草稿
                        .and(i -> i.eq(Article.COL_IS_PRIVATE, false)
                                .eq(Article.COL_IS_DRAFT, false)));

        articles.stream()
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
                                                Integer.parseInt(y.substring(0, y.length() - 1))), Collectors.toList()))))
                .forEach(res::put);

        return res;
    }

    /**
     * 查询文章，有文章内容
     *
     * @return
     */
    public ArticleContent selectContentArticleById(Integer id) {
        //文章内容
        return articleContentMapper.selectOne(new QueryWrapper<ArticleContent>().eq(ArticleContent.COL_ARTICLE_ID, id));
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

    /**
     * 添加文章
     *
     * @param data
     */
    public void newArticle(AddArticleDTO data) {
        Article article = new Article();
        BeanUtils.copyProperties(data, article);

        int insert = articleMapper.insert(article);
        Check.affectedOneRow(insert).orElseThrow(() -> new AppException(ADD_ARTICLE_FAIL));
        insert = articleContentMapper.insert(
                ArticleContent.builder()
                        .articleId(article.getId())
                        .content(data.getContent())
                        .build());
        Check.affectedOneRow(insert).orElseThrow(() -> new AppException(ADD_ARTICLE_FAIL));


    }

    /**
     * 删除文章
     *
     * @param articleId
     * @param userId
     */
    public void deleteArticle(Integer articleId, Integer userId) {
        Article article = articleMapper.selectById(articleId);
        Check.ifCorrect(article.getUserId().equals(userId)).orElseThrow(() -> new AppException(ILLEGAL_OPERATION));

        Check.affectedOneRow(
                articleMapper.deleteById(articleId)
        ).orElseThrow(() -> new AppException(DELETE_ARTICLE_FAIL));

        Check.affectedOneRow(
                articleContentMapper.delete(
                        new UpdateWrapper<ArticleContent>().lambda().eq(ArticleContent::getArticleId, article.getId()))
        ).orElseThrow(() -> new AppException(DELETE_ARTICLE_FAIL));


    }

    /**
     * 编辑文章
     *
     * @param id
     * @param data
     */
    public void editArticle(Integer id, AddArticleDTO data) {
        //校验文章
        Article articleDo = articleMapper.selectById(id);
        Optional.ofNullable(articleDo).orElseThrow(() -> new AppException(ARTICLE_NOT_EXIST));

        Check.ifCorrect(data.getUserId().equals(articleDo.getUserId())).orElseThrow(() -> new AppException(ILLEGAL_OPERATION));

        Article updateArticle = Article.builder()
                .id(id)
                .title(data.getTitle())
                .summary(data.getSummary())
                .categoryId(data.getCategoryId())
                .readPassword(data.getReadPassword())
                .isPrivate(data.getIsPrivate())
                .isDraft(data.getIsDraft())
                .tag(data.getTag())
                .img(data.getImg())
                .isUp(data.getIsUp())
                .isSupport(data.getIsSupport())
                .build();

        int update = articleMapper.updateById(updateArticle);
        Check.affectedOneRow(update).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));

        update = articleContentMapper.update(
                ArticleContent.builder().content(data.getContent()).build(),
                new UpdateWrapper<ArticleContent>().lambda()
                        .eq(ArticleContent::getArticleId, id)
        );
        Check.affectedOneRow(update).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));
    }
}
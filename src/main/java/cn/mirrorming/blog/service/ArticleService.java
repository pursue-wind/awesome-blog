package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.mapper.auto.ArticleContentMapper;
import cn.mirrorming.blog.mapper.auto.ArticleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author mirror
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleContentMapper articleContentMapper;

    /**
     * 查询所有文章，无内容
     */
    public List<Article> selectAllArticle(int cur, int size) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .orderByDesc(Article.COL_CREATED)
                //文章不是私有，也不是草稿
                .and(i -> i.eq(Article.COL_IS_PRIVATE, false).eq(Article.COL_IS_DRAFT, false));
        IPage<Article> articlePage = articleMapper.selectPage(new Page<>(cur, size), queryWrapper);
        return articlePage.getRecords();
    }

    /**
     * 查询文章，有文章内容
     */
    public void selectArticleById(Integer id) {
        Article article = articleMapper.selectById(id);

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


package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.dto.article.ArticleDto;
import cn.mirrorming.blog.domain.dto.article.ArticleListDto;
import cn.mirrorming.blog.domain.dto.base.ResultData;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.event.ArticleClickEvent;
import cn.mirrorming.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author mirror
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@RestController
@RequestMapping("article")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleController {
    private final ArticleService articleService;
    private final ApplicationContext applicationContext;

    /**
     * 文章列表
     */
    @GetMapping("all")
    public List selectAllArticle() {
        List<ArticleListDto> articleListDtos = articleService.selectAllArticle(1, 10);
        return articleListDtos;
    }

    /**
     * 归档页面文章按时间显示
     */
    @GetMapping("archives")
    public ResultData archives() {
        HashMap<Integer, Map<Integer, List<Article>>> map = articleService.filedArticle();
        return ResultData.succeed(map);
    }

    /**
     * 点击文章获得文章所有信息
     *
     * @param id           文章id
     * @param readPassword 文章阅读密码
     */
    @GetMapping("{id}")
    public ResultData selectArticleById(@PathVariable Integer id,
                                        @RequestParam(value = "readPassword", required = false) String readPassword) {
        ArticleDto articleDto = articleService.selectArticleById(id, readPassword == null ? "" : readPassword);
        applicationContext.publishEvent(new ArticleClickEvent(this, id, readPassword));
        return ResultData.succeed(articleDto);
    }
}
package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author mirror
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("test")
    public String test() {
        return "article_ok";
    }

    @GetMapping("all")
    public List selectAllArticle() {
        List<Article> articles = articleService.selectAllArticle(1, 10);
        return articles;
    }
}

package cn.mirrorming.blog;

import cn.mirrorming.blog.domain.dto.article.ArticleListDto;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    ArticleService articleService;

    @Test
    void filedArticle() {
        HashMap<Integer, Map<Integer, List<Article>>> map = articleService.filedArticle();
        System.out.println(map);
    }

    @Test
    void selectAllArticle() {
        List<ArticleListDto> articleListDtos = articleService.selectAllArticle(1, 10);
        System.out.println(articleListDtos);
    }

}

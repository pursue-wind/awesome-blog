package cn.mirrorming.blog.event.article;

import cn.mirrorming.blog.utils.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/19 20:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleChangeEventListener {
    private static final int NEW_ARTICLE = -1;
    private final RedisOperator redisOperator;

    @EventListener
    @Async
    public void articleChange(ArticleChangeEvent event) {
        //新增文章
        if (event.getId().equals(NEW_ARTICLE)) {
            redisOperator.deleteByPrex("article::ArticleList:*");
        }
        //修改文章
        else {
            redisOperator.deleteByPrex("article::ArticleList:*");
            redisOperator.deleteByPrex("article::ArticleContent:" + event.getId() + ":*");
        }
    }
}

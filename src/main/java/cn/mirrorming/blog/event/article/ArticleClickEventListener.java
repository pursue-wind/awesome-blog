package cn.mirrorming.blog.event.article;

import cn.mirrorming.blog.utils.RedisOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static cn.mirrorming.blog.domain.constants.RedisKeyConstants.ARTICLE_CLICK_EXPIRED_PREFIX;
import static cn.mirrorming.blog.domain.constants.RedisKeyConstants.ARTICLE_CLICK_PREFIX;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/19 20:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleClickEventListener {
    private final RedisOperator redisOperator;
    private static final int TIME_OUT = 10;


    /**
     * 文章点击之后，存入文章id 和 对应的点击数到 redis 5分钟后没有增加则去更新数据库的点击数
     *
     * @param event
     */
    @EventListener
    @Async
    public void articleClick(ArticleClickEvent event) {
        log.info("[event] 文章点击 ==> [{}]", event.toString());
        Integer id = event.getId();
        redisOperator.set(ARTICLE_CLICK_EXPIRED_PREFIX + id, "1", TIME_OUT);
        Integer times = Integer.valueOf(Optional.ofNullable(redisOperator.get(ARTICLE_CLICK_PREFIX + id)).orElse("0"));
        redisOperator.set(ARTICLE_CLICK_PREFIX + id, String.valueOf(++times), TIME_OUT + 35);
    }
}

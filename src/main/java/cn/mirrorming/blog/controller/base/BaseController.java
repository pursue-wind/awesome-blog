package cn.mirrorming.blog.controller.base;

import cn.mirrorming.blog.event.article.ArticleChangeEvent;
import cn.mirrorming.blog.event.article.ArticleClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.CompletableFuture;

/**
 * @author Mireal Chan
 * @Date 2019/12/6 11:33
 * @since v1.0
 */
public abstract class BaseController {
    @Autowired
    protected ApplicationContext applicationContext;

    /**
     * 文章点击事件
     */
    protected void publishArticleClickEvent(Integer id) {
        CompletableFuture.runAsync(() -> {
            applicationContext.publishEvent(new ArticleClickEvent(this, id));
        });
    }

    /**
     * 文章编辑事件，清缓存
     */
    protected void publishArticleEditEvent() {
        CompletableFuture.runAsync(() -> {
            applicationContext.publishEvent(new ArticleChangeEvent(this));
        });
    }
}

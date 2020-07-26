package cn.mirrorming.blog.event.article;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/19 20:40
 */
@Getter
public class ArticleClickEvent extends ApplicationEvent {
    private Integer id;

    public ArticleClickEvent(Object source, Integer id) {
        super(source);
        this.id = id;
    }
}
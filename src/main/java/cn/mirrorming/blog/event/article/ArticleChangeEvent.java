package cn.mirrorming.blog.event.article;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/19 20:40
 */
@Getter
public class ArticleChangeEvent extends ApplicationEvent {
    private static final int NEW_ARTICLE = -1;
//    private static final int EDITOR_ARTICLE = 2;

    /**
     * 默认为新增文章，否则为修改文章
     */
    private Integer id;

    /**
     * 新增文章，无需文章id
     *
     * @param source
     */
    public ArticleChangeEvent(Object source) {
        super(source);
        this.id = NEW_ARTICLE;
    }

    /**
     * 修改文章，仅删除对应文章id的缓存
     *
     * @param source
     */
    public ArticleChangeEvent(Object source, Integer id) {
        super(source);
        this.id = id;
    }

}
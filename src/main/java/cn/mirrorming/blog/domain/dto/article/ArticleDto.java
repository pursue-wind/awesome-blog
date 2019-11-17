package cn.mirrorming.blog.domain.dto.article;

import cn.mirrorming.blog.domain.dto.user.UserDto;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.domain.po.ArticleContent;
import cn.mirrorming.blog.domain.po.Category;
import cn.mirrorming.blog.domain.po.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/17 16:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    private Article article;
    private ArticleContent articleContent;
    private UserDto user;
    private Category category;
    private List<Tag> tags;
}
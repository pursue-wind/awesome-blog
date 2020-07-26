package cn.mirrorming.blog.domain.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2020/5/2 11:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddArticleDTO {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 文章名称
     */
    private String title;

    /**
     * 发布IP
     */
    private String ip;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 文章的模式:1为私有，0为公开
     */
    private Boolean isPrivate;

    /**
     * 访问密码
     */
    private String readPassword;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 是否草稿:0为否，1为是
     */
    private Boolean isDraft;

    /**
     * 标签
     */
    private String tag;

    /**
     * 得分，评分人数
     */
    private String score;

    /**
     * 文章配图
     */
    private String img;

    /**
     * 是否置顶:0为否，1为是
     */
    private Boolean isUp;

    /**
     * 是否博主推荐:0为否，1为是
     */
    private Boolean isSupport;

    /**
     * 文章内容 markdown
     */
    private String content;
}

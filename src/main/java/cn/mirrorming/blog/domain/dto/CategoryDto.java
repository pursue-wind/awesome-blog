package cn.mirrorming.blog.domain.dto;

import cn.mirrorming.blog.domain.po.Category;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author mireal
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    /**
     * 文章自增ID
     */
    private Integer id;

    /**
     * 父级分类ID
     */
    private Integer parentId;

    /**
     * 该分类所属用户
     */
    private Integer userId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类图片
     */
    private String avatar;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 目录对象
     */
    private List<Category> categories;


    public CategoryDto(Category category, List<Category> categories) {
        this.id = category.getId();
        this.parentId = category.getParentId();
        this.userId = category.getUserId();
        this.name = category.getName();
        this.avatar = category.getAvatar();
        this.createTime = category.getCreateTime();
        this.updateTime = category.getUpdateTime();
        this.categories = categories;
    }
}
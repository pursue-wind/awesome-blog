package cn.mirrorming.blog.domain.dto;

import cn.mirrorming.blog.domain.po.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Mireal Chan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
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
    private List<CategoryDTO> children;


    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.parentId = category.getParentId();
        this.userId = category.getUserId();
        this.name = category.getName();
        this.avatar = category.getAvatar();
        this.createTime = category.getCreateTime();
        this.updateTime = category.getUpdateTime();
    }

    public CategoryDTO(Category category, List<CategoryDTO> children) {
        this.id = category.getId();
        this.parentId = category.getParentId();
        this.userId = category.getUserId();
        this.name = category.getName();
        this.avatar = category.getAvatar();
        this.createTime = category.getCreateTime();
        this.updateTime = category.getUpdateTime();
        this.children = children;
    }
}
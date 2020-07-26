package cn.mirrorming.blog.domain.vo;

import cn.mirrorming.blog.domain.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Mireal Chan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVO {
    /**
     * 文章自增ID
     */
    private Integer id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 目录对象
     */
    private List<CategoryVO> children;

    public CategoryVO(CategoryDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        if (!CollectionUtils.isEmpty(dto.getChildren())) {
            this.children = dto.getChildren().stream().map(CategoryVO::new).collect(Collectors.toList());
        }
    }
}
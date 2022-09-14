package com.minzheng.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/**
 * 分类
 *
 * @author c
 * @date 2021/08/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 分类名
     */
    private String text;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 分类下的文章数量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

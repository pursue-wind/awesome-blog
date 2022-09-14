package com.minzheng.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * 后台分类
 *
 * @author c
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryTreeDTO {
    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


    private ArrayList<CategoryTreeDTO> children;

}

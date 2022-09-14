package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.dto.CategoryDTO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.CategoryBackDTO
import com.minzheng.blog.entity.Category
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 分类
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface CategoryDao : BaseMapper<Category> {
    /**
     * 查询分类和对应文章数量
     *
     * @return 分类列表
     */
    fun listCategoryDTO(): List<CategoryDTO>?

    /**
     * 查询后台分类列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return [<] 分类列表
     */
    fun listCategoryBackDTO(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("condition") condition: ConditionVO?
    ): List<CategoryBackDTO>?
}
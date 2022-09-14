package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.TagBackDTO
import com.minzheng.blog.entity.Tag
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 标签
 *
 * @author c
 * @date 2021/08/01
 * @since 2020-05-18
 */
@Repository
interface TagDao : BaseMapper<Tag> {
    /**
     * 查询后台标签列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return [<] 标签列表
     */
    fun listTagBackDTO(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("condition") condition: ConditionVO?
    ): List<TagBackDTO>?

    /**
     * 根据文章id查询标签名
     *
     * @param articleId 文章id
     * @return [<] 标签名列表
     */
    fun listTagNameByArticleId(articleId: Int?): List<String>?
}
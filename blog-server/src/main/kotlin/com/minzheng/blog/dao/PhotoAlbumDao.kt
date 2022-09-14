package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.PhotoAlbum
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.PhotoAlbumBackDTO
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 相册映射器
 *
 * @author c
 * @date 2021/08/04
 */
@Repository
interface PhotoAlbumDao : BaseMapper<PhotoAlbum> {
    /**
     * 查询后台相册列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return [&lt; PhotoAlbumBackDTO &gt;][List] 相册列表
     */
    fun listPhotoAlbumBacks(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("condition") condition: ConditionVO?
    ): List<PhotoAlbumBackDTO>?
}
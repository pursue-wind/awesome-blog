package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.UniqueView
import com.minzheng.blog.dto.UniqueViewDTO
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository
import java.util.*

/**
 * 访问量
 *
 * @author xiaojie
 * @date 2021/08/10
 * @since 2020-05-18
 */
@Repository
interface UniqueViewDao : BaseMapper<UniqueView> {
    /**
     * 获取7天用户量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户量
     */
    fun listUniqueViews(@Param("startTime") startTime: Date, @Param("endTime") endTime: Date): List<UniqueViewDTO>
}
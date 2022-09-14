package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.Photo
import org.springframework.stereotype.Repository

/**
 * 照片映射器
 *
 * @author c
 * @date 2021/08/04
 */
@Repository
interface PhotoDao : BaseMapper<Photo>
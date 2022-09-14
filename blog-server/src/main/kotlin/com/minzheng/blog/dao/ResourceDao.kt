package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.Resource
import org.springframework.stereotype.Repository

/**
 * 资源
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface ResourceDao : BaseMapper<Resource>
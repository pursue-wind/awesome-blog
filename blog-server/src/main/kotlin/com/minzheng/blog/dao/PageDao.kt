package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.Page
import org.springframework.stereotype.Repository

/**
 * 页面
 *
 * @author c
 * @date 2021/08/07
 */
@Repository
interface PageDao : BaseMapper<Page>
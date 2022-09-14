package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.WebsiteConfig
import org.springframework.stereotype.Repository

/**
 * 网站配置
 *
 * @author c
 * @date 2021/08/09
 */
@Repository
interface WebsiteConfigDao : BaseMapper<WebsiteConfig>
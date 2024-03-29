package com.minzheng.blog.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.dao.WebsiteConfigDao
import com.minzheng.blog.entity.WebsiteConfig
import org.springframework.stereotype.Service

/**
 * 网站配置服务
 *
 * @author c
 * @date 2021/08/09
 */
@Service
class WebsiteConfigService : ServiceImpl<WebsiteConfigDao, WebsiteConfig>()
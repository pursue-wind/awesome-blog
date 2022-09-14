package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.Message
import org.springframework.stereotype.Repository

/**
 * 留言
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface MessageDao : BaseMapper<Message>
package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.ChatRecord
import org.springframework.stereotype.Repository

/**
 * 聊天记录
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface ChatRecordDao : BaseMapper<ChatRecord>
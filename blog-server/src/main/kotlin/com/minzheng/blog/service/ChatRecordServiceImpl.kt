package com.minzheng.blog.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.dao.ChatRecordDao
import com.minzheng.blog.entity.ChatRecord
import org.springframework.stereotype.Service

/**
 * 聊天记录服务
 *
 * @author c
 * @date 2021/07/28
 */
@Service
class ChatRecordServiceImpl : ServiceImpl<ChatRecordDao, ChatRecord>()
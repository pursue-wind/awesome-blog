package com.minzheng.blog.service

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.dao.MessageDao
import javax.servlet.http.HttpServletRequest
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import com.minzheng.blog.vo.MessageVO
import com.minzheng.blog.util.IpUtils
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.constant.CommonConst
import com.minzheng.blog.dto.MessageDTO
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.minzheng.blog.vo.ReviewVO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.MessageBackDTO
import com.minzheng.blog.entity.Message
import com.minzheng.blog.util.PageUtils
import com.minzheng.blog.vo.PageResult
import org.springframework.stereotype.Service
import java.util.Objects

/**
 * 留言服务
 *
 * @author c
 * @date 2021/07/28
 */
@Service
class MessageService(
    private val messageDao: MessageDao,
    private val request: HttpServletRequest,
    private val blogInfoService: BlogInfoService
) : ServiceImpl<MessageDao, Message>() {

    @Transactional(rollbackFor = [Exception::class])
    fun saveMessage(messageVO: MessageVO) {
        // 判断是否需要审核
        val isReview = blogInfoService.getWebsiteConfig().isMessageReview
        // 获取用户ip
        val ipAddress = IpUtils.getIpAddress(request)
        val ipSource = IpUtils.getIpSource(ipAddress)
        val message = BeanCopyUtils.copyObject(messageVO, Message::class.java)
        message.ipAddress = ipAddress
        message.isReview = if (isReview == CommonConst.TRUE) CommonConst.FALSE else CommonConst.TRUE
        message.ipSource = ipSource
        messageDao.insert(message)
    }

    fun listMessages(): List<MessageDTO> {
        // 查询留言列表
        val messageList = messageDao.selectList(
            QueryWrapper<Message>().eq("is_review", CommonConst.TRUE)
        )
        return BeanCopyUtils.copyList(messageList, MessageDTO::class.java)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateMessagesReview(reviewVO: ReviewVO) {
        // 修改留言审核状态
        val messageList = reviewVO.idList?.map { item: Int? ->
            Message().let {
                it.id = item
                it.isReview = reviewVO.isReview
                it
            }
        }

        this.updateBatchById(messageList)
    }

    fun listMessageBackDTO(condition: ConditionVO): PageResult<MessageBackDTO> {
        // 分页查询留言列表
        val page = Page<Message>(PageUtils.getCurrent(), PageUtils.getSize())
        val messageLambdaQueryWrapper = QueryWrapper<Message>()
            .like(StringUtils.isNotBlank(condition.keywords), "nickname", condition.keywords)
            .eq(Objects.nonNull(condition.isReview), "is_review", condition.isReview)
            .orderByDesc("id")
        val messagePage = messageDao.selectPage(page, messageLambdaQueryWrapper)
        // 转换DTO
        val messageBackDTOList = BeanCopyUtils.copyList(messagePage.records, MessageBackDTO::class.java)
        return PageResult(messageBackDTOList, messagePage.total)
    }
}
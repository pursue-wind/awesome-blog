package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.vo.CommentVO
import com.minzheng.blog.dto.CommentDTO
import com.minzheng.blog.dto.ReplyDTO
import com.minzheng.blog.dto.ReplyCountDTO
import com.minzheng.blog.dto.CommentCountDTO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.CommentBackDTO
import com.minzheng.blog.entity.Comment
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 评论
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface CommentDao : BaseMapper<Comment> {
    /**
     * 查看评论
     *
     * @param current   当前页码
     * @param size      大小
     * @param commentVO 评论信息
     * @return 评论集合
     */
    fun listComments(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("commentVO") commentVO: CommentVO?
    ): List<CommentDTO>?

    /**
     * 查看评论id集合下的回复
     *
     * @param commentIdList 评论id集合
     * @return 回复集合
     */
    fun listReplies(@Param("commentIdList") commentIdList: List<Int>?): List<ReplyDTO>?

    /**
     * 查看当条评论下的回复
     *
     * @param commentId 评论id
     * @param current   当前页码
     * @param size      大小
     * @return 回复集合
     */
    fun listRepliesByCommentId(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("commentId") commentId: Int?
    ): List<ReplyDTO>?

    /**
     * 根据评论id查询回复总量
     *
     * @param commentIdList 评论id集合
     * @return 回复数量
     */
    fun listReplyCountByCommentId(@Param("commentIdList") commentIdList: List<Int>?): List<ReplyCountDTO>?

    /**
     * 根据评论主题id获取评论量
     *
     * @param topicIdList 说说id列表
     * @return [<]说说评论量
     */
    fun listCommentCountByTopicIds(topicIdList: List<Int>?): List<CommentCountDTO>?

    /**
     * 查询后台评论
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 评论集合
     */
    fun listCommentBackDTO(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("condition") condition: ConditionVO?
    ): List<CommentBackDTO>?

    /**
     * 统计后台评论数量
     *
     * @param condition 条件
     * @return 评论数量
     */
    fun countCommentDTO(@Param("condition") condition: ConditionVO?): Long?
}
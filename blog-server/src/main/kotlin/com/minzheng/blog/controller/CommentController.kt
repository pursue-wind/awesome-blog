package com.minzheng.blog.controller

import com.minzheng.blog.service.CommentService
import io.swagger.annotations.ApiOperation
import com.minzheng.blog.vo.CommentVO
import com.minzheng.blog.dto.CommentDTO
import javax.validation.Valid
import io.swagger.annotations.ApiImplicitParam
import com.minzheng.blog.dto.ReplyDTO
import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.vo.ReviewVO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.CommentBackDTO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.Result
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.*

/**
 * 评论控制器
 *
 * @author xiaojie
 * @date 2021/07/29
 */
@Api(tags = ["评论模块"])
@RestController
class CommentController(
    private val commentService: CommentService
) {

    /**
     * 查询评论
     *
     * @param commentVO 评论信息
     * @return [<]
     */
    @ApiOperation(value = "查询评论")
    @GetMapping("/comments")
    fun listComments(commentVO: CommentVO?): Result<PageResult<CommentDTO>> {
        return Result.ok(commentService.listComments(commentVO))
    }

    /**
     * 添加评论
     *
     * @param commentVO 评论信息
     * @return [<]
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("/comments")
    fun saveComment(@RequestBody commentVO: @Valid CommentVO?): Result<*> {
        commentService.saveComment(commentVO)
        return Result.ok<Any>()
    }

    /**
     * 查询评论下的回复
     *
     * @param commentId 评论id
     * @return [<] 回复列表
     */
    @ApiOperation(value = "查询评论下的回复")
    @ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "Integer")
    @GetMapping("/comments/{commentId}/replies")
    fun listRepliesByCommentId(@PathVariable("commentId") commentId: Int?): Result<List<ReplyDTO>> {
        return Result.ok(commentService.listRepliesByCommentId(commentId))
    }

    /**
     * 评论点赞
     *
     * @param commentId 评论id
     * @return [<]
     */
    @ApiOperation(value = "评论点赞")
    @PostMapping("/comments/{commentId}/like")
    fun saveCommentLike(@PathVariable("commentId") commentId: Int?): Result<*> {
        commentService.saveCommentLike(commentId)
        return Result.ok<Any>()
    }

    /**
     * 审核评论
     *
     * @param reviewVO 审核信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    fun updateCommentsReview(@RequestBody reviewVO: @Valid ReviewVO?): Result<*> {
        commentService.updateCommentsReview(reviewVO)
        return Result.ok<Any>()
    }

    /**
     * 删除评论
     *
     * @param commentIdList 评论id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comments")
    fun deleteComments(@RequestBody commentIdList: List<Int>?): Result<*> {
        commentService.removeByIds(commentIdList)
        return Result.ok<Any>()
    }

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return [<] 后台评论
     */
    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    fun listCommentBackDTO(condition: ConditionVO?): Result<PageResult<CommentBackDTO>> {
        return Result.ok(commentService.listCommentBackDTO(condition))
    }
}
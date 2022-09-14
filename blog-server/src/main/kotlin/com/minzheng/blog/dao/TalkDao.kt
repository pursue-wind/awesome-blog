package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.Talk
import com.minzheng.blog.dto.TalkDTO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.TalkBackDTO
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Repository

/**
 * 说说
 *
 * @author c
 * @date 2022/01/23
 */
@Repository
interface TalkDao : BaseMapper<Talk> {
    /**
     * 获取说说列表
     *
     * @param current 页码
     * @param size    大小
     * @return [<]
     */
    fun listTalks(@Param("current") current: Long?, @Param("size") size: Long?): List<TalkDTO>?

    /**
     * 查看后台说说
     *
     * @param current 页码
     * @param size    大小
     * @return [<]
     */
    fun listBackTalks(
        @Param("current") current: Long?,
        @Param("size") size: Long?,
        @Param("condition") condition: ConditionVO?
    ): List<TalkBackDTO>?

    /**
     * 根据id查看说说
     *
     * @param talkId 说说id
     * @return [TalkDTO] 说说信息
     */
    fun getTalkById(@Param("talkId") talkId: Int?): TalkDTO?

    /**
     * 根据id查看后台说说
     *
     * @param talkId 说说id
     * @return [TalkBackDTO] 说说信息
     */
    fun getBackTalkById(@Param("talkId") talkId: Int?): TalkBackDTO?
}
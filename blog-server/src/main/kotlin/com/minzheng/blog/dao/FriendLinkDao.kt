package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.FriendLink
import org.springframework.stereotype.Repository

/**
 * 友情链接
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface FriendLinkDao : BaseMapper<FriendLink>
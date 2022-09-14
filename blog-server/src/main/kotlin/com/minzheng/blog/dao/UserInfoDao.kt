package com.minzheng.blog.dao

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.minzheng.blog.entity.UserInfo
import org.springframework.stereotype.Repository

/**
 * 用户信息
 *
 * @author c
 * @date 2021/08/10
 */
@Repository
interface UserInfoDao : BaseMapper<UserInfo>
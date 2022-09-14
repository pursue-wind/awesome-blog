package com.minzheng.blog.service

import com.alibaba.fastjson.JSON
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.constant.RedisPrefixConst
import com.minzheng.blog.dao.UserInfoDao
import com.minzheng.blog.dto.UserDetailDTO
import com.minzheng.blog.dto.UserOnlineDTO
import com.minzheng.blog.entity.UserInfo
import com.minzheng.blog.entity.UserRole
import com.minzheng.blog.enums.FilePathEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.strategy.context.UploadStrategyContext
import com.minzheng.blog.util.PageUtils
import com.minzheng.blog.util.UserUtils
import com.minzheng.blog.vo.*
import org.springframework.security.core.session.SessionInformation
import org.springframework.security.core.session.SessionRegistry
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * 用户信息服务
 *
 * @author c
 */
@Service
class UserInfoService(
    private val userInfoDao: UserInfoDao,
    private val userRoleService: UserRoleService,
    private val sessionRegistry: SessionRegistry,
    private val redisService: RedisService,
    private val uploadStrategyContext: UploadStrategyContext
) : ServiceImpl<UserInfoDao, UserInfo>() {

    @Transactional(rollbackFor = [Exception::class])
    fun updateUserInfo(userInfoVO: UserInfoVO) {
        // 封装用户信息
        val userInfo = UserInfo().apply {
            id = UserUtils.getLoginUser().userInfoId
            nickname = userInfoVO.nickname
            intro = userInfoVO.intro
            webSite = userInfoVO.webSite
        }
        userInfoDao.updateById(userInfo)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateUserAvatar(file: MultipartFile): String {
        // 头像上传
        val avatar2 = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.path)
        // 更新用户信息
        val userInfo = UserInfo().apply {
            id = UserUtils.getLoginUser().userInfoId
            avatar = avatar2
        }

        userInfoDao.updateById(userInfo)
        return avatar2
    }

    @Transactional(rollbackFor = [Exception::class])
    fun saveUserEmail(emailVO: EmailVO) {
        if (emailVO.code != redisService.get(RedisPrefixConst.USER_CODE_KEY + emailVO.email).toString()) {
            throw BizException("验证码错误！")
        }
        val userInfo = UserInfo().apply {
            id = UserUtils.getLoginUser().userInfoId
            email = emailVO.email
        }

        userInfoDao.updateById(userInfo)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateUserRole(userRoleVO: UserRoleVO) {
        // 更新用户角色和昵称
        val userInfo = UserInfo().apply {
            id = userRoleVO.userInfoId
            nickname = userRoleVO.nickname
        }

        userInfoDao.updateById(userInfo)
        // 删除用户角色重新添加
        userRoleService.remove(
            KtUpdateWrapper(UserRole::class.java).eq(UserRole::id, userRoleVO.userInfoId)
        )
        val userRoleList = userRoleVO.roleIdList.stream()
            .map { rid: Int ->
                UserRole().apply {
                    roleId = rid
                    userId = userRoleVO.userInfoId
                }
            }
            .collect(Collectors.toList())
        userRoleService.saveBatch(userRoleList)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateUserDisable(userDisableVO: UserDisableVO) {
        // 更新用户禁用状态
        val userInfo = UserInfo().apply {
            id = userDisableVO.id
            isDisable = userDisableVO.isDisable
        }
        userInfoDao.updateById(userInfo)
    }

    fun listOnlineUsers(conditionVO: ConditionVO): PageResult<UserOnlineDTO> {
        // 获取security在线session
        val userOnlineDTOList = sessionRegistry!!.allPrincipals.stream()
            .filter { item: Any -> sessionRegistry.getAllSessions(item, false).size > 0 }
            .map { item: Any -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDTO::class.java) }
            .filter { item: UserOnlineDTO ->
                StringUtils.isBlank(conditionVO.keywords) || item.nickname.contains(
                    conditionVO.keywords
                )
            }
            .sorted(Comparator.comparing { obj: UserOnlineDTO -> obj.lastLoginTime }
                .reversed())
            .collect(Collectors.toList())
        // 执行分页
        val fromIndex = PageUtils.getLimitCurrent().toInt()
        val size = PageUtils.getSize().toInt()
        val toIndex = if (userOnlineDTOList.size - fromIndex > size) fromIndex + size else userOnlineDTOList.size
        val userOnlineList = userOnlineDTOList.subList(fromIndex, toIndex)
        return PageResult(userOnlineList, userOnlineDTOList.size.toLong())
    }

    fun removeOnlineUser(userInfoId: Int) {
        // 获取用户session
        val userInfoList = sessionRegistry!!.allPrincipals.stream().filter { item: Any ->
            val userDetailDTO = item as UserDetailDTO
            userDetailDTO.userInfoId == userInfoId
        }.collect(Collectors.toList())
        val allSessions: MutableList<SessionInformation> = ArrayList()
        userInfoList.forEach(Consumer { item: Any ->
            allSessions.addAll(
                sessionRegistry.getAllSessions(item, false)
            )
        })
        // 注销session
        allSessions.forEach(Consumer { obj: SessionInformation -> obj.expireNow() })
    }
}
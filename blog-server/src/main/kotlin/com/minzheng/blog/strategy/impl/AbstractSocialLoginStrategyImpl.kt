package com.minzheng.blog.strategy.impl

import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import com.minzheng.blog.constant.CommonConst.TRUE
import com.minzheng.blog.dao.UserAuthDao
import com.minzheng.blog.dao.UserInfoDao
import com.minzheng.blog.dao.UserRoleDao
import com.minzheng.blog.dto.SocialTokenDTO
import com.minzheng.blog.dto.SocialUserInfoDTO
import com.minzheng.blog.dto.UserDetailDTO
import com.minzheng.blog.dto.UserInfoDTO
import com.minzheng.blog.entity.UserAuth
import com.minzheng.blog.entity.UserInfo
import com.minzheng.blog.entity.UserRole
import com.minzheng.blog.enums.RoleEnum
import com.minzheng.blog.enums.ZoneEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.service.UserDetailsServiceImpl
import com.minzheng.blog.strategy.SocialLoginStrategy
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.util.IpUtils.getIpAddress
import com.minzheng.blog.util.IpUtils.getIpSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.servlet.http.HttpServletRequest


/**
 * 第三方登录抽象模板
 *
 * @author c
 * @date 2021/07/28
 */
@Service
abstract class AbstractSocialLoginStrategyImpl : SocialLoginStrategy {
    @Autowired
    lateinit var userAuthDao: UserAuthDao

    @Autowired
    lateinit var userInfoDao: UserInfoDao

    @Autowired
    lateinit var userRoleDao: UserRoleDao

    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    lateinit var request: HttpServletRequest


    override fun login(data: String): UserInfoDTO {
// 创建登录信息
        val userDetailDTO: UserDetailDTO
        // 获取第三方token信息
        val socialToken: SocialTokenDTO = getSocialToken(data)
        // 获取用户ip信息
        val ipAddress = getIpAddress(request)
        val ipSource = getIpSource(ipAddress)
        // 判断是否已注册
        val user: UserAuth = getUserAuth(socialToken)
        if (Objects.nonNull(user)) {
// 返回数据库用户信息
            userDetailDTO = getUserDetail(user, ipAddress!!, ipSource!!)
        } else {
// 获取第三方用户信息，保存到数据库返回
            userDetailDTO = saveUserDetail(socialToken, ipAddress!!, ipSource!!)
        }
        // 判断账号是否禁用
        if (userDetailDTO.isDisable == TRUE) {
            throw BizException("账号已被禁用")
        }
        // 将登录信息放入springSecurity管理
        val auth = UsernamePasswordAuthenticationToken(userDetailDTO, null, userDetailDTO.authorities)
        SecurityContextHolder.getContext().authentication = auth
        // 返回用户信息
        return BeanCopyUtils.copyObject(userDetailDTO, UserInfoDTO::class.java)
    }


    /**
     * 获取第三方token信息
     *
     * @param data 数据
     * @return [SocialTokenDTO] 第三方token信息
     */
    abstract fun getSocialToken(data: String): SocialTokenDTO

    /**
     * 获取第三方用户信息
     *
     * @param socialTokenDTO 第三方token信息
     * @return [SocialUserInfoDTO] 第三方用户信息
     */
    abstract fun getSocialUserInfo(socialTokenDTO: SocialTokenDTO): SocialUserInfoDTO

    /**
     * 获取用户账号
     *
     * @return {@link UserAuth} 用户账号
     */
    fun getUserAuth(socialTokenDTO: SocialTokenDTO): UserAuth {
        return userAuthDao.selectOne(
            KtQueryWrapper(UserAuth::class.java)
                .eq(UserAuth::username, socialTokenDTO.getOpenId())
                .eq(UserAuth::loginType, socialTokenDTO.getLoginType())
        )
    }

    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailDTO} 用户信息
     */
    fun getUserDetail(user: UserAuth, ipAddress: String, ipSource: String): UserDetailDTO {
// 更新登录信息
        userAuthDao.update(
            UserAuth(), KtUpdateWrapper(UserAuth::class.java)
                .set(UserAuth::lastLoginTime, LocalDateTime.now())
                .set(UserAuth::ipAddress, ipAddress)
                .set(UserAuth::ipSource, ipSource)
                .eq(UserAuth::id, user.id)
        );
        // 封装信息
        return userDetailsService.convertUserDetail(user, request);
    }


    /**
     * 新增用户信息
     *
     * @param socialToken token信息
     * @param ipAddress   ip地址
     * @param ipSource    ip源
     * @return [UserDetailDTO] 用户信息
     */
    fun saveUserDetail(socialToken: SocialTokenDTO, ipAddress2: String, ipSource2: String): UserDetailDTO {
        // 获取第三方用户信息
        val socialUserInfo: SocialUserInfoDTO = getSocialUserInfo(socialToken)
        // 保存用户信息
        val userInfo: UserInfo = UserInfo().apply {
            nickname = socialUserInfo.nickname
            avatar = socialUserInfo.avatar
        }
        userInfoDao.insert(userInfo)
        // 保存账号信息
        val userAuth: UserAuth = UserAuth().apply {
            userInfoId = userInfo.getId()
            username = socialToken.openId
            password = socialToken.accessToken
            loginType = socialToken.loginType
            lastLoginTime = LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.zone))
            ipAddress = ipAddress2
            ipSource = ipSource2
        }
        userAuthDao.insert(userAuth)
        // 绑定角色
        val userRole: UserRole = UserRole().apply {
            userId = userInfo.id
            roleId = RoleEnum.USER.roleId
        }
        userRoleDao.insert(userRole)
        return userDetailsService.convertUserDetail(userAuth, request)
    }
}
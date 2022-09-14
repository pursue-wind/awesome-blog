package com.minzheng.blog.service

import com.alibaba.fastjson.JSON
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper
import com.baomidou.mybatisplus.core.toolkit.IdWorker
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.constant.CommonConst
import com.minzheng.blog.constant.CommonConst.CITY
import com.minzheng.blog.constant.CommonConst.PROVINCE
import com.minzheng.blog.constant.CommonConst.UNKNOWN
import com.minzheng.blog.constant.MQPrefixConst
import com.minzheng.blog.constant.RedisPrefixConst
import com.minzheng.blog.dao.UserAuthDao
import com.minzheng.blog.dao.UserInfoDao
import com.minzheng.blog.dao.UserRoleDao
import com.minzheng.blog.dto.EmailDTO
import com.minzheng.blog.dto.UserAreaDTO
import com.minzheng.blog.dto.UserBackDTO
import com.minzheng.blog.dto.UserInfoDTO
import com.minzheng.blog.entity.UserAuth
import com.minzheng.blog.entity.UserInfo
import com.minzheng.blog.entity.UserRole
import com.minzheng.blog.enums.LoginTypeEnum
import com.minzheng.blog.enums.RoleEnum
import com.minzheng.blog.enums.UserAreaTypeEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.strategy.context.SocialLoginStrategyContext
import com.minzheng.blog.util.CommonUtils
import com.minzheng.blog.util.PageUtils
import com.minzheng.blog.util.UserUtils
import com.minzheng.blog.vo.*
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 用户账号服务
 *
 * @author c
 * @date 2021/08/10
 */
@Service
class UserAuthService(
    private val redisService: RedisService,
    private val userAuthDao: UserAuthDao,
    private val userRoleDao: UserRoleDao,
    private val userInfoDao: UserInfoDao,
    private val blogInfoService: BlogInfoService,
    private val rabbitTemplate: RabbitTemplate,
    private val socialLoginStrategyContext: SocialLoginStrategyContext
) : ServiceImpl<UserAuthDao, UserAuth>() {


    fun sendCode(username: String) {
        // 校验账号是否合法
        if (!CommonUtils.checkEmail(username)) {
            throw BizException("请输入正确邮箱")
        }
        // 生成六位随机验证码发送
        val code = CommonUtils.getRandomCode()
        // 发送验证码
        val emailDTO = EmailDTO().apply {
            email = username
            subject = "验证码"
            content = "您的验证码为 $code 有效期15分钟，请不要告诉他人哦！"
        }
        rabbitTemplate!!.convertAndSend(
            MQPrefixConst.EMAIL_EXCHANGE,
            "*",
            Message(JSON.toJSONBytes(emailDTO), MessageProperties())
        )
        // 将验证码存入redis，设置过期时间为15分钟

        val k = RedisPrefixConst.USER_CODE_KEY + username
        redisService.set(k, code, RedisPrefixConst.CODE_EXPIRE_TIME)
    }

    fun listUserAreas(conditionVO: ConditionVO): List<UserAreaDTO> {
        var userAreaDTOList: List<UserAreaDTO> = ArrayList()
        when (Objects.requireNonNull(UserAreaTypeEnum.getUserAreaType(conditionVO.type))) {
            UserAreaTypeEnum.USER -> {
                // 查询注册用户区域分布
                val userArea = redisService.get(RedisPrefixConst.USER_AREA)
                if (Objects.nonNull(userArea)) {
                    userAreaDTOList = JSON.parseObject<List<UserAreaDTO>>(userArea.toString(), MutableList::class.java)
                }
                return userAreaDTOList
            }
            UserAreaTypeEnum.VISITOR -> {
                // 查询游客区域分布
                val visitorArea = redisService.hGetAll(RedisPrefixConst.VISITOR_AREA)
                if (Objects.nonNull(visitorArea)) {
                    if (visitorArea != null) {//todo
                        userAreaDTOList = visitorArea.entries
                            .map { (key, value2) ->
                                UserAreaDTO().apply {
                                    name = key
                                    value = value2.toString().toLong()
                                }
                            }
                    }
                }
                return userAreaDTOList
            }
            else -> {}
        }
        return userAreaDTOList
    }

    @Transactional(rollbackFor = [Exception::class])
    fun register(user: UserVO) {
        // 校验账号是否合法
        if (checkUser(user)) {
            throw BizException("邮箱已被注册！")
        }
        // 新增用户信息
        val userInfo = UserInfo().apply {
            email = user.username
            nickname = CommonConst.DEFAULT_NICKNAME + IdWorker.getId()
            avatar = blogInfoService.getWebsiteConfig().userAvatar
        }
        userInfoDao.insert(userInfo)
        // 绑定用户角色
        val userRole: UserRole = UserRole().apply {
            userId = userInfo.id
            roleId = RoleEnum.USER.roleId
        }
        userRoleDao.insert(userRole)
        // 新增用户账号
        val userAuth = UserAuth().apply {
            userInfoId = userInfo.id
            username = user.username
            password = BCrypt.hashpw(user.password, BCrypt.gensalt())
            loginType = LoginTypeEnum.EMAIL.type
        }

        userAuthDao.insert(userAuth)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updatePassword(user: UserVO) {
        // 校验账号是否合法
        if (!checkUser(user)) {
            throw BizException("邮箱尚未注册！")
        }
        // 根据用户名修改密码
        userAuthDao.update(
            UserAuth(), LambdaUpdateWrapper<UserAuth>()
                .set(
                    SFunction<UserAuth, Any> { obj: UserAuth -> obj.password },
                    BCrypt.hashpw(user.password, BCrypt.gensalt())
                )
                .eq(SFunction<UserAuth, Any> { obj: UserAuth -> obj.username }, user.username)
        )
    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateAdminPassword(passwordVO: PasswordVO) {
        // 查询旧密码是否正确
        val user = userAuthDao.selectOne(
            LambdaQueryWrapper<UserAuth>()
                .eq(SFunction<UserAuth, Any> { obj: UserAuth -> obj.id }, UserUtils.getLoginUser().id)
        )
        // 正确则修改密码，错误则提示不正确
        if (Objects.nonNull(user) && BCrypt.checkpw(passwordVO.oldPassword, user.password)) {
            val userAuth = UserAuth().apply {
                id = UserUtils.getLoginUser().id
                password = BCrypt.hashpw(passwordVO.newPassword, BCrypt.gensalt())
            }

            userAuthDao.updateById(userAuth)
        } else {
            throw BizException("旧密码不正确")
        }
    }

    fun listUserBackDTO(condition: ConditionVO): PageResult<UserBackDTO> {
        // 获取后台用户数量
        val count = userAuthDao.countUser(condition)
        if (count == 0L) {
            return PageResult()
        }
        // 获取后台用户列表
        val userBackDTOList = userAuthDao.listUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition)
        return PageResult(userBackDTOList, count)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun qqLogin(qqLoginVO: QQLoginVO): UserInfoDTO {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLoginVO), LoginTypeEnum.QQ)
    }

    @Transactional(rollbackFor = [BizException::class])
    fun weiboLogin(weiboLoginVO: WeiboLoginVO): UserInfoDTO {
        return socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(weiboLoginVO), LoginTypeEnum.WEIBO)
    }

    /**
     * 校验用户数据是否合法
     *
     * @param user 用户数据
     * @return 结果
     */
    private fun checkUser(user: UserVO): Boolean {
        if (user.code != redisService.get(RedisPrefixConst.USER_CODE_KEY + user.username)) {
            throw BizException("验证码错误！")
        }
        //查询用户名是否存在
        val userAuth = userAuthDao.selectOne(
            LambdaQueryWrapper<UserAuth>()
                .select(SFunction<UserAuth, Any> { obj: UserAuth -> obj.username })
                .eq(SFunction<UserAuth, Any> { obj: UserAuth -> obj.username }, user.username)
        )
        return Objects.nonNull(userAuth)
    }

    /**
     * 统计用户地区
     */
    @Scheduled(cron = "0 0 * * * ?")
    fun statisticalUserArea() {
        // 统计用户地域分布
        val userAreaMap = userAuthDao.selectList(
            KtQueryWrapper(UserAuth::class.java).select(UserAuth::ipSource)
        )
            .map { item: UserAuth ->
                if (StringUtils.isNotBlank(item.ipSource)) {
                    return@map item.ipSource?.substring(0, 2)
                        ?.replace(PROVINCE.toRegex(), "")
                        ?.replace(CITY.toRegex(), "")
                }
                UNKNOWN
            }
            .groupingBy { it }
            .eachCount()

        // 转换格式
        val userAreaList = userAreaMap.entries
            .map { (key, value2) ->
                UserAreaDTO().apply {
                    name = key
                    value = value2.toLong()
                }
            }
        redisService.set(RedisPrefixConst.USER_AREA, JSON.toJSONString(userAreaList))
    }
}
package com.minzheng.blog.strategy.impl

import com.alibaba.fastjson.JSON
import com.minzheng.blog.config.QQConfigProperties
import com.minzheng.blog.constant.SocialLoginConst
import com.minzheng.blog.dto.QQTokenDTO
import com.minzheng.blog.dto.QQUserInfoDTO
import com.minzheng.blog.dto.SocialTokenDTO
import com.minzheng.blog.dto.SocialUserInfoDTO
import com.minzheng.blog.enums.LoginTypeEnum
import com.minzheng.blog.enums.StatusCodeEnum
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.util.CommonUtils
import com.minzheng.blog.vo.QQLoginVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * qq登录策略实现
 *
 * @author c
 * @date 2021/07/28
 */
@Service("qqLoginStrategyImpl")
class QQLoginStrategyImpl : AbstractSocialLoginStrategyImpl() {
    @Autowired
    lateinit var qqConfigProperties: QQConfigProperties

    @Autowired
    private val restTemplate: RestTemplate? = null
    override fun getSocialToken(data: String): SocialTokenDTO {
        val qqLoginVO = JSON.parseObject(data, QQLoginVO::class.java)
        // 校验QQ token信息
        checkQQToken(qqLoginVO)
        // 返回token信息
        return SocialTokenDTO().apply {
            openId = qqLoginVO.openId
            accessToken = qqLoginVO.accessToken
            loginType = LoginTypeEnum.QQ.type
        }
    }

    override fun getSocialUserInfo(socialTokenDTO: SocialTokenDTO): SocialUserInfoDTO {
        // 定义请求参数
        val formData: MutableMap<String, String?> = HashMap(3)
        formData[SocialLoginConst.QQ_OPEN_ID] = socialTokenDTO.openId
        formData[SocialLoginConst.ACCESS_TOKEN] = socialTokenDTO.accessToken
        formData[SocialLoginConst.OAUTH_CONSUMER_KEY] = qqConfigProperties!!.appId
        // 获取QQ返回的用户信息
        val qqUserInfoDTO = JSON.parseObject(
            restTemplate!!.getForObject(
                qqConfigProperties.userInfoUrl, String::class.java, formData
            ), QQUserInfoDTO::class.java
        )
        // 返回用户信息
        return SocialUserInfoDTO().apply {
            nickname = Objects.requireNonNull(qqUserInfoDTO).nickname
            avatar = qqUserInfoDTO.figureurl_qq_1
        }
    }

    /**
     * 校验qq token信息
     *
     * @param qqLoginVO qq登录信息
     */
    private fun checkQQToken(qqLoginVO: QQLoginVO) {
        // 根据token获取qq openId信息
        val qqData: MutableMap<String, String?> = HashMap(1)
        qqData[SocialLoginConst.ACCESS_TOKEN] = qqLoginVO.accessToken
        try {
            val result = restTemplate!!.getForObject(qqConfigProperties!!.checkTokenUrl, String::class.java, qqData)
            val qqTokenDTO =
                JSON.parseObject(CommonUtils.getBracketsContent(Objects.requireNonNull(result)), QQTokenDTO::class.java)
            // 判断openId是否一致
            if (qqLoginVO.openId != qqTokenDTO.openid) {
                throw BizException(StatusCodeEnum.QQ_LOGIN_ERROR)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw BizException(StatusCodeEnum.QQ_LOGIN_ERROR)
        }
    }
}
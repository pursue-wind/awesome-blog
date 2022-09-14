package com.minzheng.blog.vo

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import lombok.Builder
import lombok.Data

/**
 * 网站配置信息
 *
 * @author c
 * @date 2021/08/09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "网站配置")
data class WebsiteConfigVO(
    /**
     * 网站头像
     */
    @ApiModelProperty(name = "websiteAvatar", value = "网站头像", required = true, dataType = "String")
    val websiteAvatar: String? = null,

    /**
     * 网站名称
     */
    @ApiModelProperty(name = "websiteName", value = "网站名称", required = true, dataType = "String")
    val websiteName: String? = null,

    /**
     * 网站作者
     */
    @ApiModelProperty(name = "websiteAuthor", value = "网站作者", required = true, dataType = "String")
    val websiteAuthor: String? = null,

    /**
     * 网站介绍
     */
    @ApiModelProperty(name = "websiteIntro", value = "网站介绍", required = true, dataType = "String")
    val websiteIntro: String? = null,

    /**
     * 网站公告
     */
    @ApiModelProperty(name = "websiteNotice", value = "网站公告", required = true, dataType = "String")
    val websiteNotice: String? = null,

    /**
     * 网站创建时间
     */
    @ApiModelProperty(name = "websiteCreateTime", value = "网站创建时间", required = true, dataType = "LocalDateTime")
    val websiteCreateTime: String? = null,

    /**
     * 网站备案号
     */
    @ApiModelProperty(name = "websiteRecordNo", value = "网站备案号", required = true, dataType = "String")
    val websiteRecordNo: String? = null,

    /**
     * 社交登录列表
     */
    @ApiModelProperty(name = "socialLoginList", value = "社交登录列表", required = true, dataType = "List<String>")
    val socialLoginList: List<String>? = null,

    /**
     * 社交url列表
     */
    @ApiModelProperty(name = "socialUrlList", value = "社交url列表", required = true, dataType = "List<String>")
    val socialUrlList: List<String>? = null,

    /**
     * qq
     */
    @ApiModelProperty(name = "qq", value = "qq", required = true, dataType = "String")
    val qq: String? = null,

    /**
     * github
     */
    @ApiModelProperty(name = "github", value = "github", required = true, dataType = "String")
    val github: String? = null,

    /**
     * gitee
     */
    @ApiModelProperty(name = "gitee", value = "gitee", required = true, dataType = "String")
    val gitee: String? = null,

    /**
     * 游客头像
     */
    @ApiModelProperty(name = "touristAvatar", value = "游客头像", required = true, dataType = "String")
    val touristAvatar: String? = null,

    /**
     * 用户头像
     */
    @ApiModelProperty(name = "userAvatar", value = "用户头像", required = true, dataType = "String")
    val userAvatar: String? = null,

    /**
     * 是否评论审核
     */
    @ApiModelProperty(name = "isCommentReview", value = "是否评论审核", required = true, dataType = "Integer")
    val isCommentReview: Int? = null,

    /**
     * 是否留言审核
     */
    @ApiModelProperty(name = "isMessageReview", value = "是否留言审核", required = true, dataType = "Integer")
    val isMessageReview: Int? = null,

    /**
     * 是否邮箱通知
     */
    @ApiModelProperty(name = "isEmailNotice", value = "是否邮箱通知", required = true, dataType = "Integer")
    val isEmailNotice: Int? = null,

    /**
     * 是否打赏
     */
    @ApiModelProperty(name = "isReward", value = "是否打赏", required = true, dataType = "Integer")
    val isReward: Int? = null,

    /**
     * 微信二维码
     */
    @ApiModelProperty(name = "weiXinQRCode", value = "微信二维码", required = true, dataType = "String")
    val weiXinQRCode: String? = null,

    /**
     * 支付宝二维码
     */
    @ApiModelProperty(name = "alipayQRCode", value = "支付宝二维码", required = true, dataType = "String")
    val alipayQRCode: String? = null,

    /**
     * 是否开启聊天室
     */
    @ApiModelProperty(name = "isReward", value = "是否打赏", required = true, dataType = "Integer")
    val isChatRoom: Int? = null,

    /**
     * websocket地址
     */
    @ApiModelProperty(name = "websocketUrl", value = "websocket地址", required = true, dataType = "String")
    val websocketUrl: String? = null,

    /**
     * 是否开启音乐
     */
    @ApiModelProperty(name = "isMusicPlayer", value = "是否开启音乐", required = true, dataType = "Integer")
    val isMusicPlayer: Int? = null,
)
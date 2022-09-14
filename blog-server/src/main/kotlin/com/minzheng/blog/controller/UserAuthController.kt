package com.minzheng.blog.controller

import com.minzheng.blog.annotation.AccessLimit
import com.minzheng.blog.dto.UserAreaDTO
import com.minzheng.blog.dto.UserBackDTO
import com.minzheng.blog.dto.UserInfoDTO
import com.minzheng.blog.service.UserAuthService
import com.minzheng.blog.vo.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * 用户账号控制器
 *
 * @author c
 * @date 2021/07/28
 */
@Api(tags = ["用户账号模块"])
@RestController
class UserAuthController(
    private val userAuthService: UserAuthService
) {

    /**
     * 发送邮箱验证码
     *
     * @param username 用户名
     * @return [<]
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/users/code")
    fun sendCode(username: String): Result<*> {
        userAuthService.sendCode(username)
        return Result.ok<Any>()
    }

    /**
     * 获取用户区域分布
     *
     * @param conditionVO 条件
     * @return [<] 用户区域分布
     */
    @ApiOperation(value = "获取用户区域分布")
    @GetMapping("/admin/users/area")
    fun listUserAreas(conditionVO: ConditionVO): Result<List<UserAreaDTO>> {
        return Result.ok(userAuthService.listUserAreas(conditionVO))
    }

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return [<] 用户列表
     */
    @ApiOperation(value = "查询后台用户列表")
    @GetMapping("/admin/users")
    fun listUsers(condition: ConditionVO): Result<PageResult<UserBackDTO>> {
        return Result.ok(userAuthService.listUserBackDTO(condition))
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return [<]
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    fun register(@RequestBody user: @Valid UserVO): Result<*> {
        userAuthService.register(user)
        return Result.ok<Any>()
    }

    /**
     * 修改密码
     *
     * @param user 用户信息
     * @return [<]
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    fun updatePassword(@RequestBody user: @Valid UserVO): Result<*> {
        userAuthService.updatePassword(user)
        return Result.ok<Any>()
    }

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码信息
     * @return [<]
     */
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    fun updateAdminPassword(@RequestBody passwordVO: @Valid PasswordVO): Result<*> {
        userAuthService.updateAdminPassword(passwordVO)
        return Result.ok<Any>()
    }

    /**
     * 微博登录
     *
     * @param weiBoLoginVO 微博登录信息
     * @return [<] 用户信息
     */
    @ApiOperation(value = "微博登录")
    @PostMapping("/users/oauth/weibo")
    fun weiboLogin(@RequestBody weiBoLoginVO: @Valid WeiboLoginVO): Result<UserInfoDTO> {
        return Result.ok(userAuthService.weiboLogin(weiBoLoginVO))
    }

    /**
     * qq登录
     *
     * @param qqLoginVO qq登录信息
     * @return [<] 用户信息
     */
    @ApiOperation(value = "qq登录")
    @PostMapping("/users/oauth/qq")
    fun qqLogin(@RequestBody qqLoginVO: @Valid QQLoginVO): Result<UserInfoDTO> {
        return Result.ok(userAuthService.qqLogin(qqLoginVO))
    }
}
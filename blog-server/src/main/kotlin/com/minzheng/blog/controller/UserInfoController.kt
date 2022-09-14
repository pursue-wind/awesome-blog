package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.dto.UserOnlineDTO
import com.minzheng.blog.service.UserInfoService
import com.minzheng.blog.vo.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

/**
 * 用户信息控制器
 *
 * @author c
 * @date 2021/07/29
 */
@Api(tags = ["用户信息模块"])
@RestController
class UserInfoController(
    private val userInfoService: UserInfoService
) {

    /**
     * 更新用户信息
     *
     * @param userInfoVO 用户信息
     * @return [<]
     */
    @ApiOperation(value = "更新用户信息")
    @PutMapping("/users/info")
    fun updateUserInfo(@RequestBody @Valid userInfoVO: UserInfoVO): Result<*> {
        userInfoService.updateUserInfo(userInfoVO)
        return Result.ok<Any>()
    }

    /**
     * 更新用户头像
     *
     * @param file 文件
     * @return [<] 头像地址
     */
    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/users/avatar")
    fun updateUserAvatar(file: MultipartFile): Result<String> {
        return Result.ok(userInfoService.updateUserAvatar(file))
    }

    /**
     * 绑定用户邮箱
     *
     * @param emailVO 邮箱信息
     * @return [<]
     */
    @ApiOperation(value = "绑定用户邮箱")
    @PostMapping("/users/email")
    fun saveUserEmail(@RequestBody emailVO: @Valid EmailVO): Result<*> {
        userInfoService.saveUserEmail(emailVO)
        return Result.ok<Any>()
    }

    /**
     * 修改用户角色
     *
     * @param userRoleVO 用户角色信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/users/role")
    fun updateUserRole(@RequestBody @Valid userRoleVO: UserRoleVO): Result<*> {
        userInfoService.updateUserRole(userRoleVO)
        return Result.ok<Any>()
    }

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVO 用户禁用信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable")
    fun updateUserDisable(@RequestBody @Valid userDisableVO: UserDisableVO): Result<*> {
        userInfoService.updateUserDisable(userDisableVO)
        return Result.ok<Any>()
    }

    /**
     * 查看在线用户
     *
     * @param conditionVO 条件
     * @return [<] 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @GetMapping("/admin/users/online")
    fun listOnlineUsers(conditionVO: ConditionVO): Result<PageResult<UserOnlineDTO>> {
        return Result.ok(userInfoService.listOnlineUsers(conditionVO))
    }

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息
     * @return [<]
     */
    @ApiOperation(value = "下线用户")
    @DeleteMapping("/admin/users/{userInfoId}/online")
    fun removeOnlineUser(@PathVariable("userInfoId") userInfoId: Int): Result<*> {
        userInfoService.removeOnlineUser(userInfoId)
        return Result.ok<Any>()
    }
}
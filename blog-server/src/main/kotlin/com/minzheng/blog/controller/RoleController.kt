package com.minzheng.blog.controller

import com.minzheng.blog.annotation.OptLog
import com.minzheng.blog.constant.OptTypeConst
import com.minzheng.blog.dto.RoleDTO
import com.minzheng.blog.dto.UserRoleDTO
import com.minzheng.blog.service.RoleService
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.vo.PageResult
import com.minzheng.blog.vo.Result
import com.minzheng.blog.vo.RoleVO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * 角色控制器
 *
 * @author c
 * @date 2021/07/29
 */
@Api(tags = ["角色模块"])
@RestController
class RoleController(
    private val roleService: RoleService
) {

    /**
     * 查询用户角色选项
     *
     * @return [<] 用户角色选项
     */
    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    fun listUserRoles(): Result<List<UserRoleDTO>> {
        return Result.ok(roleService.listUserRoles())
    }

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return [<] 角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    fun listRoles(conditionVO: ConditionVO?): Result<PageResult<RoleDTO>> {
        return Result.ok(roleService.listRoles(conditionVO))
    }

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色信息
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    fun saveOrUpdateRole(@RequestBody roleVO: @Valid RoleVO?): Result<*> {
        roleService.saveOrUpdateRole(roleVO)
        return Result.ok<Any>()
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     * @return [<]
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    fun deleteRoles(@RequestBody roleIdList: List<Int>?): Result<*> {
        roleService.deleteRoles(roleIdList)
        return Result.ok<Any>()
    }
}
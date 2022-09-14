package com.minzheng.blog.controller

import io.swagger.annotations.Api
import lombok.RequiredArgsConstructor
import com.minzheng.blog.service.MenuService
import io.swagger.annotations.ApiOperation
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.dto.MenuDTO
import javax.validation.Valid
import com.minzheng.blog.vo.MenuVO
import com.minzheng.blog.dto.LabelOptionDTO
import com.minzheng.blog.dto.UserMenuDTO
import com.minzheng.blog.vo.Result
import org.springframework.web.bind.annotation.*

/**
 * 菜单控制器
 *
 * @author c
 */
@Api(tags = ["菜单模块"])
@RestController
@RequiredArgsConstructor
class MenuController(
    private val menuService: MenuService
) {

    /**
     * 查询菜单列表
     *
     * @param conditionVO 条件
     * @return [<] 菜单列表
     */
    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menus")
    fun listMenus(conditionVO: ConditionVO?): Result<List<MenuDTO>> {
        return Result.ok(menuService.listMenus(conditionVO))
    }

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单
     * @return [<]
     */
    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/admin/menus")
    fun saveOrUpdateMenu(@RequestBody menuVO: @Valid MenuVO?): Result<*> {
        menuService.saveOrUpdateMenu(menuVO)
        return Result.ok<Any>()
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return [<]
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/menus/{menuId}")
    fun deleteMenu(@PathVariable("menuId") menuId: Int?): Result<*> {
        menuService.deleteMenu(menuId)
        return Result.ok<Any>()
    }

    /**
     * 查看角色菜单选项
     *
     * @return [<] 查看角色菜单选项
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role/menus")
    fun listMenuOptions(): Result<List<LabelOptionDTO>> {
        return Result.ok(menuService.listMenuOptions())
    }

    /**
     * 查看当前用户菜单
     *
     * @return [<] 菜单列表
     */
    @ApiOperation(value = "查看当前用户菜单")
    @GetMapping("/admin/user/menus")
    fun listUserMenus(): Result<List<UserMenuDTO>> {
        return Result.ok(menuService.listUserMenus())
    }
}
package com.minzheng.blog.controller

import com.minzheng.blog.dto.LabelOptionDTO
import com.minzheng.blog.dto.ResourceDTO
import com.minzheng.blog.service.ResourceService
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.vo.ResourceVO
import com.minzheng.blog.vo.Result
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * 资源控制器
 *
 * @author c
 * @date 2021/07/27
 */
@Api(tags = ["资源模块"])
@RestController
class ResourceController(
    private val resourceService: ResourceService
) {
    @ApiOperation(value = "导入swagger接口")
    @GetMapping("/admin/resources/import/swagger")
    fun importSwagger(): Result<*> {
        resourceService.importSwagger()
        return Result.ok<Any>()
    }

    /**
     * 查看资源列表
     *
     * @return [<] 资源列表
     */
    @ApiOperation(value = "查看资源列表")
    @GetMapping("/admin/resources")
    fun listResources(conditionVO: ConditionVO?): Result<List<ResourceDTO>> {
        return Result.ok(resourceService.listResources(conditionVO))
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     * @return [<]
     */
    @ApiOperation(value = "删除资源")
    @DeleteMapping("/admin/resources/{resourceId}")
    fun deleteResource(@PathVariable("resourceId") resourceId: Int?): Result<*> {
        resourceService.deleteResource(resourceId)
        return Result.ok<Any>()
    }

    /**
     * 新增或修改资源
     *
     * @param resourceVO 资源信息
     * @return [<]
     */
    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/admin/resources")
    fun saveOrUpdateResource(@RequestBody resourceVO: @Valid ResourceVO?): Result<*> {
        resourceService.saveOrUpdateResource(resourceVO)
        return Result.ok<Any>()
    }

    /**
     * 查看角色资源选项
     *
     * @return [<] 角色资源选项
     */
    @ApiOperation(value = "查看角色资源选项")
    @GetMapping("/admin/role/resources")
    fun listResourceOption(): Result<List<LabelOptionDTO>> {
        return Result.ok(resourceService.listResourceOption())
    }
}
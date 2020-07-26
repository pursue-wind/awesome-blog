package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.aop.logger.Log;
import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.po.Category;
import cn.mirrorming.blog.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mireal Chan
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "分类")
@RestController
@RequestMapping("category")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {
    private final CategoryService categoryService;

    @Log(value = "所有的分类")
    @ApiOperation(value = "所有的分类")
    @GetMapping("all")
    public R selectAllCategory() {
        return R.succeed(categoryService.selectAllCategory());
    }

    @Log(value = "获得某个 id 的所有子分类")
    @ApiOperation(value = "获得某个 id 的所有子分类")
    @GetMapping("parent-all/{id}")
    public R selectCategorysByParentId(@PathVariable("id") Integer parentId) {
        List<Category> parentCategorys = categoryService.selectCategorysByParentId(parentId);
        return R.succeed(parentCategorys);
    }

    @Log(value = "添加分类")
    @ApiOperation(value = "添加分类")
    @PostMapping("add")
    public R addCategory(Category category) {
        categoryService.addCategory(category);
        return R.succeed();
    }

    /**
     * 修改分类
     */
    @ApiOperation(value = "修改分类")
    @Log(value = "修改分类")
    @PutMapping("update")
    public R updateCategory(Category category) {
        categoryService.updateCategory(category);
        return R.succeed();
    }
}
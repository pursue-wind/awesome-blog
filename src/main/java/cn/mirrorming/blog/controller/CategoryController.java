package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.po.Category;
import cn.mirrorming.blog.service.CategoryService;
import cn.mirrorming.blog.domain.dto.base.ResultData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author mirror
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("category")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * 所有的分类
     */
    @GetMapping("all")
    public ResultData selectAllCategory() {
        return ResultData.succeed(categoryService.selectAllCategory());
    }

    /**
     * 所有父级的分类
     */
    @GetMapping("parent-all")
    public ResultData selectAllFatherCategory() {
        List<Category> parentCategorys = categoryService.selectAllFatherCategory();
        return ResultData.succeed(parentCategorys);
    }

    /**
     * 添加分类
     */
    @PostMapping("add")
    public ResultData addCategory(Category category) {
        return categoryService.addCategory(category) ?
                ResultData.succeed() : ResultData.fail("添加失败");
    }

    /**
     * 删除分类
     */
    @DeleteMapping("delete/{id}")
    public ResultData deleteCategory(@PathVariable Integer id) {
        return categoryService.deleteCategory(id) ?
                ResultData.succeed() : ResultData.fail("删除失败");
    }

    /**
     * 修改分类
     */
    @PutMapping("update")
    public ResultData updateCategory(Category category) {
        return categoryService.updateCategory(category) ?
                ResultData.succeed() : ResultData.fail("修改失败");
    }
}
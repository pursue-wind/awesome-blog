package cn.mirrorming.blog.controller.admin;

import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.po.Category;
import cn.mirrorming.blog.security.SecurityUtil;
import cn.mirrorming.blog.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mireal Chan
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "admin分类")
@RestController
@RequestMapping("admin/category")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminCategoryController {
    private final CategoryService categoryService;

    /**
     * 所有的分类
     */
    @GetMapping("all")
    public R selectAllCategory() {
        return R.succeed(categoryService.selectAllCategory());
    }

    /**
     * 添加分类
     */
    @PostMapping("add")
    public R addCategory(Category category) {
        return R.succeed();
    }

    /**
     * 删除分类
     */
    @DeleteMapping("delete/{id}")
    public R deleteCategory(@PathVariable Integer id) {
        int userId = SecurityUtil.getUserId();
        categoryService.deleteCategory(id, userId);
        return R.succeed();
    }

    /**
     * 目录的id -> 修改目录名字和目录的父级id
     * 修改分类
     */
    @PutMapping("update")
    public R updateCategory(@RequestBody Category category) {
        int userId = SecurityUtil.getUserId();
        category.setUserId(userId);
        categoryService.updateCategory(category);
        return R.succeed();
    }
}
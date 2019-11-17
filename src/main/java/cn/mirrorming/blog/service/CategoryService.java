package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.CategoryDto;
import cn.mirrorming.blog.domain.po.Category;
import cn.mirrorming.blog.mapper.auto.CategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.mirrorming.blog.domain.constants.SystemConstant.EFFECT_ROW;


/**
 * @Author mirror
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {
    /**
     * 父级 id 为 0 表示为根目录
     */
    private final static int PARENT_ID = 0;

    private final CategoryMapper categoryMapper;


    /**
     * 获得所有父级分类
     */
    public List<Category> selectAllFatherCategory() {
        return categoryMapper.selectList(
                new QueryWrapper<Category>()
                        .orderByDesc(Category.COL_NAME)
                        .and(i -> i.eq(Category.COL_PARENT_ID, PARENT_ID))
        );
    }

    /**
     * 获得所有分类，树状排列
     */
    public List<CategoryDto> selectAllCategory() {
        //所有父级分类
        List<Category> categories = categoryMapper.selectList(
                new QueryWrapper<Category>()
                        .orderByDesc(Category.COL_NAME)
                        .and(i -> i.eq(Category.COL_PARENT_ID, PARENT_ID))
        );
        return categories.stream().map(category -> {
            List<Category> categoryList = categoryMapper.selectList(
                    new QueryWrapper<Category>()
                            .and(i -> i.eq(Category.COL_PARENT_ID, category.getId()))
            );
            return new CategoryDto(category, categoryList);
        }).collect(Collectors.toList());
    }

    /**
     * 添加目录
     */
    public boolean addCategory(Category category) {
        return categoryMapper.insert(category) == EFFECT_ROW;
    }

    /**
     * 删除目录
     */
    public boolean deleteCategory(Integer id) {
        return categoryMapper.deleteById(id) == EFFECT_ROW;
    }

    /**
     * 修改目录
     */
    public boolean updateCategory(Category category) {
        return categoryMapper.updateById(category) == EFFECT_ROW;
    }
}

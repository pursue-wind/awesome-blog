package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.CategoryDTO;
import cn.mirrorming.blog.domain.po.Category;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.mapper.generate.CategoryMapper;
import cn.mirrorming.blog.utils.Check;
import cn.mirrorming.blog.utils.ExceptionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.mirrorming.blog.domain.enums.RespEnum.*;


/**
 * @author Mireal Chan
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@Service
@CacheConfig(cacheNames = "category")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {
    /**
     * 父级 id 为 0 表示为根目录
     */
    private final static int PARENT_ID = 0;

    private final CategoryMapper categoryMapper;


    /**
     * 获得所有父级分类
     *
     * @param parentId
     */
    public List<Category> selectCategorysByParentId(Integer parentId) {
        return categoryMapper.selectList(
                new QueryWrapper<Category>()
                        .orderByDesc(Category.COL_NAME)
                        .and(i -> i.eq(Category.COL_PARENT_ID, parentId))
        );
    }

    /**
     * 获得所有分类，树状排列
     */
//    @Cacheable(key = "'AllCategory'")
    public List<CategoryDTO> selectAllCategory() {
        //所有父级分类
        return categoryMapper.selectList(
                new QueryWrapper<Category>()
                        .orderByDesc(Category.COL_NAME)
                        .and(i -> i.eq(Category.COL_PARENT_ID, PARENT_ID)))
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList())
                .stream()
                .map(this::getAllCategory)
                .collect(Collectors.toList());
    }

    /**
     * 获得所有分类，树状排列，递归调用
     */
    private CategoryDTO getAllCategory(CategoryDTO categoryDto) {
        //查询这个 CategoryDto 有没有子目录
        List<CategoryDTO> collect = categoryMapper.selectList(
                new QueryWrapper<Category>()
                        .and(i -> i.eq(Category.COL_PARENT_ID, categoryDto.getId())))
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {
            categoryDto.setChildren(collect);
            for (CategoryDTO c : collect) {
                getAllCategory(c);
            }
        } else {
            categoryDto.setChildren(Lists.newArrayList());
        }
        return categoryDto;
    }

    /**
     * 添加目录
     */
    public void addCategory(Category category) {
        int insert = categoryMapper.insert(category);
        Check.affectedOneRow(insert).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));
    }

    /**
     * 删除目录
     */
    public void deleteCategory(Integer id, Integer userId) {
        Category category = categoryMapper.selectById(id);
        Optional.ofNullable(category).orElseThrow(() -> new AppException("目录不存在"));

        Check.ifCorrect(!userId.equals(category.getUserId())).orElseThrow(() -> new AppException(DELETE_FAILED));

        List<Category> categories = categoryMapper.selectList(
                new QueryWrapper<Category>().and(i -> i.eq(Category.COL_PARENT_ID, id)));

        Check.ifCorrect(!CollectionUtils.isEmpty(categories)).orElseThrow(() -> new AppException(CANNOT_DELETE));

        Check.affectedOneRow(categoryMapper.deleteById(id)).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));
    }

    /**
     * 修改目录
     */
//    @CacheEvict(value = "category", key = "'AllCategory'")
    public void updateCategory(Category category) {
        Check.affectedOneRow(
                categoryMapper.update(category,
                        new UpdateWrapper<Category>().eq(Category.COL_USER_ID, category.getUserId()))
        ).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));
    }
}

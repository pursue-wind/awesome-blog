package com.minzheng.blog.service

import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.minzheng.blog.dao.ArticleDao
import com.minzheng.blog.dao.CategoryDao
import com.minzheng.blog.dto.*
import com.minzheng.blog.entity.Article
import com.minzheng.blog.entity.Category
import com.minzheng.blog.exception.BizException
import com.minzheng.blog.util.BeanCopyUtils
import com.minzheng.blog.util.ListToTree
import com.minzheng.blog.util.PageUtils
import com.minzheng.blog.vo.CategoryVO
import com.minzheng.blog.vo.ConditionVO
import com.minzheng.blog.vo.PageResult
import org.springframework.stereotype.Service
import java.util.*

/**
 * 分类服务
 *
 * @author xiaojie
 * @date 2021/07/29
 */
@Service
class CategoryService(
    private val articleDao: ArticleDao
) : ServiceImpl<CategoryDao, Category>() {

     fun listCategories(): PageResult<CategoryDTO> {
        return PageResult(baseMapper.listCategoryDTO(), baseMapper.selectCount(null))
    }

     fun listCategoriesTree(): List<CategoryTreeDTO> {
        val categorys = baseMapper.listCategoryDTO()
        return ListToTree.buildTree(categorys, 0)
    }

     fun listCategoriesMindTree(): List<CategoryMindTreeVo>? {
        val categorys = baseMapper.selectList(null)
        return CategoryMindTreeUtils.buildVo(categorys)
    }

     fun listBackCategories(condition: ConditionVO): PageResult<CategoryBackDTO> {
        // 查询分类数量
        val count = baseMapper.selectCount(
            KtQueryWrapper(Category::class.java)
                .like(
                    StringUtils.isNotBlank(condition.keywords),
                    Category::categoryName,
                    condition.keywords
                )
        )
        if (count == 0L) {
            return PageResult()
        }
        // 分页查询分类列表
        val categoryList = baseMapper.listCategoryBackDTO(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition)
        return PageResult(categoryList, count)
    }

     fun listCategoriesBySearch(condition: ConditionVO): List<CategoryOptionDTO> {
        // 搜索分类
        val categoryList = baseMapper.selectList(
            KtQueryWrapper(Category::class.java)
                .like(
                    StringUtils.isNotBlank(condition.keywords),
                    Category::categoryName,
                    condition.keywords
                )
                .orderByDesc(Category::id)
        )
        return BeanCopyUtils.copyList(categoryList, CategoryOptionDTO::class.java)
    }

     fun deleteCategory(categoryIdList: List<Int>) {
        // 查询分类id下是否有文章
        val count = articleDao.selectCount(
            KtQueryWrapper(Article::class.java)
                .`in`(Article::categoryId, categoryIdList)
        )
        if (count > 0) {
            throw BizException("删除失败，该分类下存在文章")
        }
        baseMapper.deleteBatchIds(categoryIdList)
    }

     fun saveOrUpdateCategory(categoryVO: CategoryVO) {
        // 判断分类名重复
        val existCategory = baseMapper.selectOne(
            KtQueryWrapper(Category::class.java)
                .select(Category::id)
                .eq(Category::categoryName, categoryVO.categoryName)
        )
        if (Objects.nonNull(existCategory) && existCategory.id != categoryVO.id) {
            throw BizException("分类名已存在")
        }
        val category = Category().apply {
            id = categoryVO.id
            categoryName = categoryVO.categoryName
            parentId = categoryVO.parentId
        }
        this.saveOrUpdate(category)
    }
}
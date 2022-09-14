package com.minzheng.blog.dto

import com.minzheng.blog.entity.Category
import com.minzheng.blog.util.BeanCopyUtils
import kotlin.collections.ArrayList

data class CategoryMindTree(
    /**
     * id
     */
    var id: Int? = null,
    /**
     * 父分类id
     */
    var parentId: Int? = null,
    /**
     * 分类名
     */
    var text: String? = null,
)


data class CategoryMindTreeVo(
    val data: CategoryMindTree? = null,
    var children: MutableList<CategoryMindTreeVo>? = null
)


object CategoryMindTreeUtils {
    fun buildVo(categories: List<Category>): List<CategoryMindTreeVo>? {
        return buildTree(categories, 0)
    }

    private fun buildTree(list: List<Category>, pId: Int?): List<CategoryMindTreeVo>? {
        val tree: MutableList<CategoryMindTreeVo> = ArrayList()
        for (category in list) {
            val treeDTO = CategoryMindTree().apply {
                id = category.id
                parentId = category.parentId
                text = category.categoryName
            }
            if (treeDTO.parentId == pId) {
                val treeVo = CategoryMindTreeVo(treeDTO, ArrayList())
                tree.add(findChild(treeVo, list))
            }
        }
        return tree
    }

    fun findChild(treeVo: CategoryMindTreeVo, list: List<Category>): CategoryMindTreeVo {
        val childrenList: MutableList<CategoryMindTreeVo> = ArrayList()
        for (category in list) {
            if (category.parentId == treeVo.data?.id) {

                val mindTree = CategoryMindTree().apply {
                    id = category.id
                    parentId = category.parentId
                    text = category.categoryName
                }

                if (treeVo.children == null) {
                    treeVo.children = ArrayList()
                }
                val vo = CategoryMindTreeVo(mindTree, ArrayList())
                treeVo.children!!.add(vo)
            }
        }
        return treeVo
    }

}


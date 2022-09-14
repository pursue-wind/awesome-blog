package com.minzheng.blog.util;

import com.minzheng.blog.dto.CategoryDTO;
import com.minzheng.blog.dto.CategoryMindTreeVo;
import com.minzheng.blog.dto.CategoryTreeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListToTree {

    /**
     * 使用递归将数组转为树形结构
     * 父ID属性为parent
     */
    public static List<CategoryTreeDTO> buildTree(List<CategoryDTO> list, Integer parentId) {
        List<CategoryTreeDTO> tree = new ArrayList<>();
        for (CategoryDTO dto : list) {
            CategoryTreeDTO treeDTO = BeanCopyUtils.copyObject(dto, CategoryTreeDTO.class);
            if (Objects.equals(treeDTO.getParentId(), parentId)) {
                tree.add(findChild(treeDTO, list));
            }
        }
        return tree;
    }


    public static CategoryTreeDTO findChild(CategoryTreeDTO treeDTO, List<CategoryDTO> list) {
        for (CategoryDTO dto : list) {
            if (Objects.equals(dto.getParentId(), treeDTO.getId())) {
                if (treeDTO.getChildren() == null) {
                    treeDTO.setChildren(new ArrayList<>());
                }
                CategoryTreeDTO treeDTO2 = BeanCopyUtils.copyObject(dto, CategoryTreeDTO.class);
                treeDTO.getChildren().add(findChild(treeDTO2, list));
            }
        }
        return treeDTO;
    }


}

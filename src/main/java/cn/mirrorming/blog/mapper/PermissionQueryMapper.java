package cn.mirrorming.blog.mapper;

import cn.mirrorming.blog.domain.po.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Mireal Chan
 * @Date 2019/9/4 16:52
 * @since v1.0.0
 */
public interface PermissionQueryMapper {
    /**
     * 根据用户 id 查询权限列表
     *
     * @param userId
     * @return
     */
    List<Permission> selectPermissionByUserId(@Param("id") Integer userId);
}

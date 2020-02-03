package cn.mirrorming.blog.mapper;

import cn.mirrorming.blog.domain.po.SecurityPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Mireal
 * @Date 2019/9/4 16:52
 * @since v1.0.0
 */
public interface PermissionQueryMapper {

    @Select("SELECT  " +
            "  p.*   " +
            "FROM  " +
            "  users AS u  " +
            "  LEFT JOIN security_user_role AS ur ON u.id = ur.user_id  " +
            "  LEFT JOIN security_role AS r ON r.id = ur.role_id  " +
            "  LEFT JOIN security_role_permission AS rp ON r.id = rp.role_id  " +
            "  LEFT JOIN security_permission AS p ON p.id = rp.permission_id   " +
            "WHERE  " +
            "  u.id = #{id}")
    List<SecurityPermission> selectPermissionByUserId(@Param("id") Integer id);
}

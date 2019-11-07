package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.SecurityRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SecurityRolePermissionMapper extends BaseMapper<SecurityRolePermission> {
    int updateBatch(List<SecurityRolePermission> list);

    int batchInsert(@Param("list") List<SecurityRolePermission> list);

    int insertOrUpdate(SecurityRolePermission record);

    int insertOrUpdateSelective(SecurityRolePermission record);
}
package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.SecurityUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SecurityUserRoleMapper extends BaseMapper<SecurityUserRole> {
    int updateBatch(List<SecurityUserRole> list);

    int batchInsert(@Param("list") List<SecurityUserRole> list);

    int insertOrUpdate(SecurityUserRole record);

    int insertOrUpdateSelective(SecurityUserRole record);
}
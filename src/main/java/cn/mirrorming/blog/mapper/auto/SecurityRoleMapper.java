package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.SecurityRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SecurityRoleMapper extends BaseMapper<SecurityRole> {
    int updateBatch(List<SecurityRole> list);

    int batchInsert(@Param("list") List<SecurityRole> list);

    int insertOrUpdate(SecurityRole record);

    int insertOrUpdateSelective(SecurityRole record);
}
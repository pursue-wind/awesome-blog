package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.SecurityPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SecurityPermissionMapper extends BaseMapper<SecurityPermission> {
    int updateBatch(List<SecurityPermission> list);

    int batchInsert(@Param("list") List<SecurityPermission> list);

    int insertOrUpdate(SecurityPermission record);

    int insertOrUpdateSelective(SecurityPermission record);
}
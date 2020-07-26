package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.po.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    int updateBatch(List<Permission> list);

    int updateBatchSelective(List<Permission> list);

    int batchInsert(@Param("list") List<Permission> list);

    int insertOrUpdate(Permission record);

    int insertOrUpdateSelective(Permission record);
}
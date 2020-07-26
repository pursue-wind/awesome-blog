package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.po.RolePermissionRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RolePermissionRelationMapper extends BaseMapper<RolePermissionRelation> {
    int updateBatch(List<RolePermissionRelation> list);

    int updateBatchSelective(List<RolePermissionRelation> list);

    int batchInsert(@Param("list") List<RolePermissionRelation> list);

    int insertOrUpdate(RolePermissionRelation record);

    int insertOrUpdateSelective(RolePermissionRelation record);
}
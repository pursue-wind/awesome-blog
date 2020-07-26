package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.po.UserRoleRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelation> {
    int updateBatch(List<UserRoleRelation> list);

    int updateBatchSelective(List<UserRoleRelation> list);

    int batchInsert(@Param("list") List<UserRoleRelation> list);

    int insertOrUpdate(UserRoleRelation record);

    int insertOrUpdateSelective(UserRoleRelation record);
}
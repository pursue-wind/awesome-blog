package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.po.Social;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SocialMapper extends BaseMapper<Social> {
    int updateBatch(List<Social> list);

    int updateBatchSelective(List<Social> list);

    int batchInsert(@Param("list") List<Social> list);

    int insertOrUpdate(Social record);

    int insertOrUpdateSelective(Social record);
}
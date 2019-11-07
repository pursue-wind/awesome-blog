package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TagMapper extends BaseMapper<Tag> {
    int updateBatch(List<Tag> list);

    int batchInsert(@Param("list") List<Tag> list);

    int insertOrUpdate(Tag record);

    int insertOrUpdateSelective(Tag record);
}
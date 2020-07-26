package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.po.Music;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MusicMapper extends BaseMapper<Music> {
    int updateBatch(List<Music> list);

    int updateBatchSelective(List<Music> list);

    int batchInsert(@Param("list") List<Music> list);

    int insertOrUpdate(Music record);

    int insertOrUpdateSelective(Music record);
}
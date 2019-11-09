package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.Music;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MusicMapper extends BaseMapper<Music> {
    int updateBatch(List<Music> list);

    int batchInsert(@Param("list") List<Music> list);

    int insertOrUpdate(Music record);

    int insertOrUpdateSelective(Music record);
}
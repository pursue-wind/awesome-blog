package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.MusicList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MusicListMapper extends BaseMapper<MusicList> {
    int updateBatch(List<MusicList> list);

    int batchInsert(@Param("list") List<MusicList> list);

    int insertOrUpdate(MusicList record);

    int insertOrUpdateSelective(MusicList record);
}
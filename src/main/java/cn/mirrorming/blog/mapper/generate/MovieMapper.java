package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.po.Movie;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MovieMapper extends BaseMapper<Movie> {
    int updateBatch(List<Movie> list);

    int updateBatchSelective(List<Movie> list);

    int batchInsert(@Param("list") List<Movie> list);

    int insertOrUpdate(Movie record);

    int insertOrUpdateSelective(Movie record);
}
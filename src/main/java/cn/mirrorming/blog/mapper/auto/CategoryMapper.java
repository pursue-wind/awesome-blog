package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategoryMapper extends BaseMapper<Category> {
    int updateBatch(List<Category> list);

    int batchInsert(@Param("list") List<Category> list);

    int insertOrUpdate(Category record);

    int insertOrUpdateSelective(Category record);
}
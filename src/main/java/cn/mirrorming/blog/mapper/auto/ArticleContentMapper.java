package cn.mirrorming.blog.mapper.auto;

import cn.mirrorming.blog.domain.po.ArticleContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ArticleContentMapper extends BaseMapper<ArticleContent> {
    int updateBatch(List<ArticleContent> list);

    int batchInsert(@Param("list") List<ArticleContent> list);

    int insertOrUpdate(ArticleContent record);

    int insertOrUpdateSelective(ArticleContent record);
}
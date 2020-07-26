package cn.mirrorming.blog.mapper.generate;

import cn.mirrorming.blog.domain.po.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    int updateBatch(List<Article> list);

    int updateBatchSelective(List<Article> list);

    int batchInsert(@Param("list") List<Article> list);

    int insertOrUpdate(Article record);

    int insertOrUpdateSelective(Article record);

    /**
     * 统计文章所有字数
     */
    @Select("SELECT SUM(num) FROM (SELECT char_length(content) num  from article_content) AS tbnum")
    String selectWordNumberSum();
}
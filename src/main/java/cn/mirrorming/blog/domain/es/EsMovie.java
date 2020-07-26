package cn.mirrorming.blog.domain.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Jsoup爬虫电影实体
 *
 * @author Mireal Chan
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "movie_index", type = "movie")
public class EsMovie implements Serializable {
    @Id
    private Integer id;
    //片名
    private String name;

    //封面图片
    private String coverUrl;

    //影片截图
    private String screenshot;

    //译名
    private List<String> translatedName;

    //标题
    private String title;

    //年代
    private String year;

    //产地
    private String origin;

    //类别
    private List<String> category;

    //上映日期
    private String releaseDate;

    //豆瓣评分
    private String score;

    //片长
    private String duration;

    //导演
    private String director;

    //主演
    private List<String> actor;

    //简介
    private String description;

    //下载地址
    private List<String> downloadUrl;
}
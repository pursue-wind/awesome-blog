package cn.mirrorming.blog.domain.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

/**
 @author Mireal Chan
 * @date 2019-04-09
 */
@Data
@Document(indexName = "article_index", type = "article")
public class EsArticle {
    @Id
    private Integer id;
    private String userName;
    private String avatar;
    private String title;
    private String summary;
    private String ip;
    private Integer click;
    private Integer liked;
    private String img;
    private Date created;
    private Date updated;
    private String categoryName;
    private String content;
    private List<String> tagName;
}

package cn.mirrorming.blog.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "article_content")
public class ArticleContent implements Serializable {
    public static final String COL_ID = "id";
    public static final String COL_CONTENT = "content";
    public static final String COL_CONTENT_HTML = "content_html";
    public static final String COL_ARTICLE_ID = "article_id";
    /**
     * 文章内容id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 文章内容html代码
     */
    @TableField(value = "content_html")
    private String contentHtml;

    /**
     * 文章id
     */
    @TableField(value = "article_id")
    private Integer articleId;

    private static final long serialVersionUID = 1L;

    public static ArticleContentBuilder builder() {
        return new ArticleContentBuilder();
    }
}
package cn.mirrorming.blog.domain.po;

import cn.mirrorming.blog.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value = "cn-mirrorming-blog-domain-po-ArticleContent")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "article_content")
public class ArticleContent extends BaseEntity implements Serializable {
    /**
     * 文章内容id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "文章内容id")
    private Integer id;

    /**
     * 文章内容
     */
    @TableField(value = "content")
    @ApiModelProperty(value = "文章内容")
    private String content;

    /**
     * 文章内容html代码
     */
    @TableField(value = "content_html")
    @ApiModelProperty(value = "文章内容html代码")
    private String contentHtml;

    /**
     * 文章id
     */
    @TableField(value = "article_id")
    @ApiModelProperty(value = "文章id")
    private Integer articleId;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_CONTENT = "content";

    public static final String COL_CONTENT_HTML = "content_html";

    public static final String COL_ARTICLE_ID = "article_id";
}
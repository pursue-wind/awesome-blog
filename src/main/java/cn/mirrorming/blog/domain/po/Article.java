package cn.mirrorming.blog.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "article")
public class Article implements Serializable {
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_TITLE = "title";
    public static final String COL_IP = "ip";
    public static final String COL_CLICK = "click";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_IS_PRIVATE = "is_private";
    public static final String COL_READ_PASSWORD = "read_password";
    public static final String COL_COMMENT_ID = "comment_id";
    public static final String COL_SUMMARY = "summary";
    public static final String COL_IS_DRAFT = "is_draft";
    public static final String COL_LIKED = "liked";
    public static final String COL_TAG = "tag";
    public static final String COL_SCORE = "score";
    public static final String COL_IMG = "img";
    public static final String COL_CREATED = "created";
    public static final String COL_UPDATED = "updated";
    public static final String COL_IS_UP = "is_up";
    public static final String COL_IS_SUPPORT = "is_support";
    /**
     * 文章ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 文章名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 发布IP
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 查看人数
     */
    @TableField(value = "click")
    private Integer click;

    /**
     * 分类id
     */
    @TableField(value = "category_id")
    private Integer categoryId;

    /**
     * 文章的模式:1为私有，0为公开
     */
    @TableField(value = "is_private")
    private Boolean isPrivate;

    /**
     * 访问密码
     */
    @TableField(value = "read_password")
    private String readPassword;

    /**
     * 文章评论id
     */
    @TableField(value = "comment_id")
    private Integer commentId;

    /**
     * 文章摘要
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 是否草稿:0为否，1为是
     */
    @TableField(value = "is_draft")
    private Boolean isDraft;

    /**
     * 点赞数
     */
    @TableField(value = "liked")
    private Integer liked;

    /**
     * 标签json
     */
    @TableField(value = "tag")
    private Object tag;

    /**
     * 得分，评分人数
     */
    @TableField(value = "score")
    private Object score;

    /**
     * 文章配图
     */
    @TableField(value = "img")
    private String img;

    /**
     * 发布时间
     */
    @TableField(value = "created")
    private Date created;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    private Date updated;

    /**
     * 是否置顶:0为否，1为是
     */
    @TableField(value = "is_up")
    private Boolean isUp;

    /**
     * 是否博主推荐:0为否，1为是
     */
    @TableField(value = "is_support")
    private Boolean isSupport;

    private static final long serialVersionUID = 1L;

    public static ArticleBuilder builder() {
        return new ArticleBuilder();
    }
}
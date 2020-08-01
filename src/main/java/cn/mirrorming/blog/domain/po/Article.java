package cn.mirrorming.blog.domain.po;

import cn.mirrorming.blog.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel(value = "cn-mirrorming-blog-domain-po-Article")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "article")
public class Article extends BaseEntity implements Serializable {
    /**
     * 文章ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "文章ID")
    private Integer id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    /**
     * 文章名称
     */
    @TableField(value = "title")
    @ApiModelProperty(value = "文章名称")
    private String title;

    /**
     * 发布IP
     */
    @TableField(value = "ip")
    @ApiModelProperty(value = "发布IP")
    private String ip;

    /**
     * 查看人数
     */
    @TableField(value = "click")
    @ApiModelProperty(value = "查看人数")
    private Integer click;

    /**
     * 分类id
     */
    @TableField(value = "category_id")
    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    /**
     * 文章的模式:1为私有，0为公开
     */
    @TableField(value = "is_private")
    @ApiModelProperty(value = "文章的模式:1为私有，0为公开")
    private Boolean isPrivate;

    /**
     * 是否博主推荐:0为否，1为是
     */
    @TableField(value = "is_support")
    @ApiModelProperty(value = "是否博主推荐:0为否，1为是")
    private Boolean isSupport;

    /**
     * 是否置顶:0为否，1为是
     */
    @TableField(value = "is_up")
    @ApiModelProperty(value = "是否置顶:0为否，1为是")
    private Boolean isUp;

    /**
     * 访问密码
     */
    @TableField(value = "read_password")
    @ApiModelProperty(value = "访问密码")
    private String readPassword;

    /**
     * 文章评论id
     */
    @TableField(value = "comment_id")
    @ApiModelProperty(value = "文章评论id")
    private Integer commentId;

    /**
     * 文章摘要
     */
    @TableField(value = "summary")
    @ApiModelProperty(value = "文章摘要")
    private String summary;

    /**
     * 是否草稿:0为否，1为是
     */
    @TableField(value = "is_draft")
    @ApiModelProperty(value = "是否草稿:0为否，1为是")
    private Boolean isDraft;

    /**
     * 点赞数
     */
    @TableField(value = "liked")
    @ApiModelProperty(value = "点赞数")
    private Integer liked;

    /**
     * 标签
     */
    @TableField(value = "tag")
    @ApiModelProperty(value = "标签")
    private String tag;

    /**
     * 得分，评分人数
     */
    @TableField(value = "score")
    @ApiModelProperty(value = "得分，评分人数")
    private String score;

    /**
     * 文章配图
     */
    @TableField(value = "img")
    @ApiModelProperty(value = "文章配图")
    private String img;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_TITLE = "title";

    public static final String COL_IP = "ip";

    public static final String COL_CLICK = "click";

    public static final String COL_CATEGORY_ID = "category_id";

    public static final String COL_IS_PRIVATE = "is_private";

    public static final String COL_IS_SUPPORT = "is_support";

    public static final String COL_IS_UP = "is_up";

    public static final String COL_READ_PASSWORD = "read_password";

    public static final String COL_COMMENT_ID = "comment_id";

    public static final String COL_SUMMARY = "summary";

    public static final String COL_IS_DRAFT = "is_draft";

    public static final String COL_LIKED = "liked";

    public static final String COL_TAG = "tag";

    public static final String COL_SCORE = "score";

    public static final String COL_IMG = "img";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
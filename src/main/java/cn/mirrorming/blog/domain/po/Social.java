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

@ApiModel(value = "cn-mirrorming-blog-domain-po-Social")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "social")
public class Social extends BaseEntity implements Serializable {
    /**
     * 社交id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "社交id")
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 社交名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "社交名称")
    private String name;

    /**
     * 链接地址
     */
    @TableField(value = "link")
    @ApiModelProperty(value = "链接地址")
    private String link;

    /**
     * 样式名字
     */
    @TableField(value = "css_name")
    @ApiModelProperty(value = "样式名字")
    private String cssName;

    /**
     * 其他
     */
    @TableField(value = "other")
    @ApiModelProperty(value = "其他")
    private String other;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_NAME = "name";

    public static final String COL_LINK = "link";

    public static final String COL_CSS_NAME = "css_name";

    public static final String COL_OTHER = "other";
}
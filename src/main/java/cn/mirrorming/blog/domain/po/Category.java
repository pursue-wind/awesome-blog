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

@ApiModel(value = "cn-mirrorming-blog-domain-po-Category")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "category")
public class Category extends BaseEntity implements Serializable {
    /**
     * 文章自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "文章自增ID")
    private Integer id;

    /**
     * 父级分类ID
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value = "父级分类ID")
    private Integer parentId;

    /**
     * 该分类所属用户
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "该分类所属用户")
    private Integer userId;

    /**
     * 分类名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 分类图片
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value = "分类图片")
    private String avatar;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_NAME = "name";

    public static final String COL_AVATAR = "avatar";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
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

/**
 * 后台用户和角色关系表
 */
@ApiModel(value = "cn-mirrorming-blog-domain-po-UserRoleRelation")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_role_relation")
public class UserRoleRelation extends BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer id;

    @TableField(value = "user_id")
    @ApiModelProperty(value = "")
    private Integer userId;

    @TableField(value = "role_id")
    @ApiModelProperty(value = "")
    private Integer roleId;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_ROLE_ID = "role_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
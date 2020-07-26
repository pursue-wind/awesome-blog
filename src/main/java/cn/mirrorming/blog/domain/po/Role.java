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
 * 后台用户角色表
 */
@ApiModel(value = "cn-mirrorming-blog-domain-po-Role")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`role`")
public class Role extends BaseEntity implements Serializable {
    public static final String COL_MODULE = "module";
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 公司id
     */
    @TableField(value = "company_id")
    @ApiModelProperty(value = "公司id")
    private Integer companyId;

    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 角色值
     */
    @TableField(value = "value")
    @ApiModelProperty(value = "角色值")
    private String value;

    /**
     * 描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     * 后台用户数量
     */
    @TableField(value = "member_count")
    @ApiModelProperty(value = "后台用户数量")
    private Integer memberCount;

    /**
     * 启用状态：0->禁用；1->启用
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "启用状态：0->禁用；1->启用")
    private Integer status;

    @TableField(value = "sort")
    @ApiModelProperty(value = "")
    private Integer sort;

    /**
     * 是否默认角色
     */
    @TableField(value = "is_default")
    @ApiModelProperty(value = "是否默认角色")
    private Boolean isDefault;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_COMPANY_ID = "company_id";

    public static final String COL_NAME = "name";

    public static final String COL_VALUE = "value";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_MEMBER_COUNT = "member_count";

    public static final String COL_STATUS = "status";

    public static final String COL_SORT = "sort";

    public static final String COL_IS_DEFAULT = "is_default";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
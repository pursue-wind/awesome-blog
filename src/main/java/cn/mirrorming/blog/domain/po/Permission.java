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
 * 后台用户权限表
 */
@ApiModel(value = "cn-mirrorming-blog-domain-po-Permission")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "permission")
public class Permission extends BaseEntity implements Serializable {
    public static final String COL_MODULE = "module";
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 父级权限id
     */
    @TableField(value = "pid")
    @ApiModelProperty(value = "父级权限id")
    private Integer pid;

    /**
     * 名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 权限值
     */
    @TableField(value = "value")
    @ApiModelProperty(value = "权限值")
    private String value;

    /**
     * 图标
     */
    @TableField(value = "icon")
    @ApiModelProperty(value = "图标")
    private String icon;

    /**
     * 权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）")
    private Integer type;

    /**
     * 前端资源路径
     */
    @TableField(value = "uri")
    @ApiModelProperty(value = "前端资源路径")
    private String uri;

    /**
     * 启用状态；0->禁用；1->启用
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "启用状态；0->禁用；1->启用")
    private Integer status;

    /**
     * 排序
     */
    @TableField(value = "sort")
    @ApiModelProperty(value = "排序")
    private Integer sort;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_PID = "pid";

    public static final String COL_NAME = "name";

    public static final String COL_VALUE = "value";

    public static final String COL_ICON = "icon";

    public static final String COL_TYPE = "type";

    public static final String COL_URI = "uri";

    public static final String COL_STATUS = "status";

    public static final String COL_SORT = "sort";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
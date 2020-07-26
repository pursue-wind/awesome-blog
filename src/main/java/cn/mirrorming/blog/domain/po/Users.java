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

@ApiModel(value = "cn-mirrorming-blog-domain-po-Users")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "users")
public class Users extends BaseEntity implements Serializable {
    public static final String COL_IS_ADMIN = "is_admin";
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID")
    private Integer id;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    /**
     * 用户密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value = "用户密码")
    private String password;

    /**
     * 用户名
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "用户名")
    private String name;

    /**
     * 用户手机号码
     */
    @TableField(value = "phone")
    @ApiModelProperty(value = "用户手机号码")
    private String phone;

    /**
     * 用户性别
     */
    @TableField(value = "sex")
    @ApiModelProperty(value = "用户性别")
    private Boolean sex;

    /**
     * 用户QQ号码
     */
    @TableField(value = "qq")
    @ApiModelProperty(value = "用户QQ号码")
    private String qq;

    /**
     * 用户地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "用户地址")
    private String address;

    /**
     * 用户积分
     */
    @TableField(value = "score")
    @ApiModelProperty(value = "用户积分")
    private Integer score;

    /**
     * 用户生日
     */
    @TableField(value = "birthday")
    @ApiModelProperty(value = "用户生日")
    private Date birthday;

    /**
     * 自我描述
     */
    @TableField(value = "description")
    @ApiModelProperty(value = "自我描述")
    private String description;

    /**
     * 头像存储路径
     */
    @TableField(value = "avatar")
    @ApiModelProperty(value = "头像存储路径")
    private String avatar;

    /**
     * 创建时间
     */
    @TableField(value = "register_time")
    @ApiModelProperty(value = "创建时间")
    private Date registerTime;

    /**
     * 更新时间
     */
    @TableField(value = "last_update_time")
    @ApiModelProperty(value = "更新时间")
    private Date lastUpdateTime;

    /**
     * 注册时IP地址
     */
    @TableField(value = "register_ip")
    @ApiModelProperty(value = "注册时IP地址")
    private String registerIp;

    /**
     * 用户微博
     */
    @TableField(value = "weibo")
    @ApiModelProperty(value = "用户微博")
    private String weibo;

    /**
     * 是否冻结，1为冻结，0为不冻结
     */
    @TableField(value = "is_freeze")
    @ApiModelProperty(value = "是否冻结，1为冻结，0为不冻结")
    private Boolean isFreeze;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_EMAIL = "email";

    public static final String COL_PASSWORD = "password";

    public static final String COL_NAME = "name";

    public static final String COL_PHONE = "phone";

    public static final String COL_SEX = "sex";

    public static final String COL_QQ = "qq";

    public static final String COL_ADDRESS = "address";

    public static final String COL_SCORE = "score";

    public static final String COL_BIRTHDAY = "birthday";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_AVATAR = "avatar";

    public static final String COL_REGISTER_TIME = "register_time";

    public static final String COL_LAST_UPDATE_TIME = "last_update_time";

    public static final String COL_REGISTER_IP = "register_ip";

    public static final String COL_WEIBO = "weibo";

    public static final String COL_IS_FREEZE = "is_freeze";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
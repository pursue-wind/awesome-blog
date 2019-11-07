package cn.mirrorming.blog.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "users")
public class Users implements Serializable, UserDetails {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 用户手机号码
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户性别
     */
    @TableField(value = "sex")
    private Boolean sex;

    /**
     * 用户QQ号码
     */
    @TableField(value = "qq")
    private String qq;

    /**
     * 用户地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 用户积分
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 用户生日
     */
    @TableField(value = "birthday")
    private Date birthday;

    /**
     * 自我描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 头像存储路径
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 创建时间
     */
    @TableField(value = "register_time")
    private Date registerTime;

    /**
     * 更新时间
     */
    @TableField(value = "last_update_time")
    private Date lastUpdateTime;

    /**
     * 注册时IP地址
     */
    @TableField(value = "register_ip")
    private String registerIp;

    /**
     * 用户微博
     */
    @TableField(value = "weibo")
    private String weibo;

    /**
     * 是否冻结，1为冻结，0为不冻结
     */
    @TableField(value = "is_freeze")
    private Boolean isFreeze;

    /**
     * 是否管理员, 1为管理，0为普通
     */
    @TableField(value = "is_admin")
    private Boolean isAdmin;

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

    public static final String COL_IS_ADMIN = "is_admin";

    public Users(String name, String password, boolean b, boolean b1, boolean b2, boolean b3, List<GrantedAuthority> grantedAuthorities) {
    }

    public static UsersBuilder builder() {
        return new UsersBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
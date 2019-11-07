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
@TableName(value = "security_role")
public class SecurityRole implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父角色
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 角色名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 角色英文名称
     */
    @TableField(value = "enname")
    private String enname;

    /**
     * 备注
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    private Date created;

    /**
     * 更新时间
     */
    @TableField(value = "updated")
    private Date updated;

    private static final long serialVersionUID = 1L;
}
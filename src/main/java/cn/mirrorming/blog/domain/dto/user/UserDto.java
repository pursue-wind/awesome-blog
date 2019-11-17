package cn.mirrorming.blog.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/17 18:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户性别
     */
    private Boolean sex;

    /**
     * 用户积分
     */
    private Integer score;

    /**
     * 用户生日
     */
    private Date birthday;

    /**
     * 自我描述
     */
    private String description;

    /**
     * 头像存储路径
     */
    private String avatar;

    /**
     * 用户微博
     */
    private String weibo;
}

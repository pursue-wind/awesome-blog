package cn.mirrorming.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/8 21:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRegisterDTO {
    @Email(message = "请填写正确的邮箱")
    private String email;
    @Size(message = "长度不能小于6", min = 6)
    private String password;
    private String key;
}

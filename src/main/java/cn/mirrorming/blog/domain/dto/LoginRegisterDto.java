package cn.mirrorming.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/8 21:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRegisterDto {
    private String email;
    private String password;
}

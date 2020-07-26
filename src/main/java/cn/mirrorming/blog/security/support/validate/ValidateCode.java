package cn.mirrorming.blog.security.support.validate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证码信息 类
 *
 * @author Mireal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = 1L;
    private String code;
    private long expireTime;

    public static ValidateCode of(String code, Integer expireIn) {
        return new ValidateCode(code, expireIn);
    }

    protected ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = System.currentTimeMillis() + expireIn * 1000;
    }

    @JsonIgnore
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }
}

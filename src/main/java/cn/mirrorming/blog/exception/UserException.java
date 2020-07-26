package cn.mirrorming.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mireal Chan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer statusCode;

    public UserException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public UserException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
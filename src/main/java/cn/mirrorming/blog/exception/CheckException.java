package cn.mirrorming.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mirror
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer statusCode;

    public CheckException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public CheckException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
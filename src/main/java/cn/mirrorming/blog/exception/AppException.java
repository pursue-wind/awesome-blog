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
public class AppException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer statusCode;

    public AppException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public AppException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

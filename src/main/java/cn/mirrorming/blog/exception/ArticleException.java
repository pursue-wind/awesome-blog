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
public class ArticleException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer statusCode;

    public ArticleException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ArticleException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

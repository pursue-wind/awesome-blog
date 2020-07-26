package cn.mirrorming.blog.exception;

import cn.mirrorming.blog.domain.enums.RespEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Mireal Chan
 */
@Data
@EqualsAndHashCode(callSuper = false)
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

    public AppException(RespEnum respEnum) {
        super(respEnum.getMsg());
        this.statusCode = respEnum.getCode();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}


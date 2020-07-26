package cn.mirrorming.blog.exception;

/**
 * @author Mireal Chan
 */
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public BusinessException() {
    }

    public BusinessException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public BusinessException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

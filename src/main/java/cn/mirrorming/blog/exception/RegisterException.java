package cn.mirrorming.blog.exception;

/**
 * @author mirror
 */
public class RegisterException extends RuntimeException {
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

    public RegisterException() {
    }

    public RegisterException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public RegisterException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

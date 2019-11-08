package cn.mirrorming.blog.exception;

/**
 * @author mirror
 */
public class UserException extends RuntimeException {
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

    public UserException() {
    }

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

package cn.mirrorming.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用数据传输对象
 * <p>
 * Description:
 * </p>
 *
 * @author Mireal
 * @Date 2019/9/6 10:19
 * @since v1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 3468352004150968551L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回对象
     */
    private T data;

    public ResponseResult(Integer code) {
        super();
        this.code = code;
    }

    public ResponseResult(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public ResponseResult(Integer code, Throwable throwable) {
        this.code = code;
        this.message = throwable.getMessage();
    }

    public ResponseResult(Integer code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    /**
     * 通用状态码
     */
    public class CodeStatus {
        /**
         * 请求成功
         */
        public static final int OK = 20000;

        /**
         * 请求失败
         */
        public static final int FAIL = 20002;

        /**
         * 熔断请求
         */
        public static final int BREAKING = 20004;

        /**
         * 非法请求
         */
        public static final int ILLEGAL_REQUEST = 50000;

        /**
         * 非法令牌
         */
        public static final int ILLEGAL_TOKEN = 50008;

        /**
         * 其他客户登录
         */
        public static final int OTHER_CLIENTS_LOGGED_IN = 50012;

        /**
         * 令牌已过期
         */
        public static final int TOKEN_EXPIRED = 50014;
    }
}

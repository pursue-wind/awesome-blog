package cn.mirrorming.blog.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 返回数据对象
 *
 * @author Mireal Chan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回码
     */
    private int statusCode;
    /**
     * 返回数据
     */
    private T content;

    private R(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public static <T> R<T> fail(String message) {
        return new R<>(message, 400, null);
    }

    public static <T> R<T> fail(int statusCode, String message) {
        return new R<>(message, statusCode, null);
    }

    public static <T> R<T> build(String message, Integer statusCode) {
        return new R<>(message, statusCode, null);
    }


    public static <T> R<T> succeed(T content, String message) {
        return new R<>(message, CommonCode.SUCCESS.getCode(), content);
    }

    public static <T> R<T> succeed(T content) {
        return new R<>(CommonCode.SUCCESS.getMessage(), CommonCode.SUCCESS.getCode(), content);
    }

    public static <T> R<T> succeed() {
        return new R<>(CommonCode.SUCCESS.getMessage(), CommonCode.SUCCESS.getCode());
    }

    public static <T> R<T> withCode(CommonCode code) {
        return new R<>(code.getMessage(), code.getCode());
    }

    public static <T> R<T> systemError(String... message) {
        String msg = CommonCode.SEVER_ERROR.getMessage();
        return new R<>(msg, CommonCode.SEVER_ERROR.getCode(), null);
    }

    @Getter
    @AllArgsConstructor
    enum CommonCode {
        // 成功
        SUCCESS("success", 200),
        // 系统错误
        SEVER_ERROR("systemFault", 500);

        private String message;
        private int code;

        public static String getMessageByCode(int code) {
            for (CommonCode enuma : values()) {
                if (enuma.getCode() == code) {
                    return enuma.getMessage();
                }
            }
            return "未知枚举项";
        }
    }
}

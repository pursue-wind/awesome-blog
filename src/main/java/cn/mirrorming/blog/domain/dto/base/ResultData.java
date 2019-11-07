package cn.mirrorming.blog.domain.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 返回数据对象
 *
 * @author mirror
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData<T> {
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

    private ResultData(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public static <T> ResultData<T> fail(String message) {
        return new ResultData<>(message, 400, null);
    }

    public static <T> ResultData<T> build(String message, Integer statusCode) {
        return new ResultData<>(message, statusCode, null);
    }


    public static <T> ResultData<T> succeed(T content, String message) {
        return new ResultData<>(message, CommonCode.SUCCESS.getCode(), content);
    }

    public static <T> ResultData<T> succeed(T content) {
        return new ResultData<>(CommonCode.SUCCESS.getMessage(), CommonCode.SUCCESS.getCode(), content);
    }

    public static <T> ResultData<T> succeed() {
        return new ResultData<>(CommonCode.SUCCESS.getMessage(), CommonCode.SUCCESS.getCode());
    }

    public static <T> ResultData<T> withCode(CommonCode code) {
        return new ResultData<>(code.getMessage(), code.getCode());
    }

    public static <T> ResultData<T> systemError(String... message) {
        String msg = CommonCode.SEVER_ERROR.getMessage();
        return new ResultData<>(msg, CommonCode.SEVER_ERROR.getCode(), null);
    }

    @Getter
    enum CommonCode {

        // 成功
        SUCCESS("success", 200),
        // 系统错误
        SEVER_ERROR("systemFault", 500);


        private String message;
        private int code;

        CommonCode(String message, int code) {
            this.message = message;
            this.code = code;
        }

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

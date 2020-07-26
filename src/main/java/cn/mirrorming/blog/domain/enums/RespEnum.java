package cn.mirrorming.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码和信息详情
 *
 * @author Mireal
 */
@AllArgsConstructor
public enum RespEnum {
    /**
     * 系统相关
     */
    OK(20000, "success"),
    TOKEN_INVALIDED(50001, "Token过期"),
    USER_NOT_EXIST(50002, "用户不存在"),
    UPDATE_FAILED(4000, "修改失败"),
    DELETE_FAILED(4001, "删除失败"),
    LOGIN_INFO_NOT_FOUND(4002, "未找到用户信息"),
    ILLEGAL_OPERATION(4003, "非法操作"),
    NICKNAME_UPDATE_FAILED(4004, "昵称修改失败"),
    USER_REGISTRATION_FAILED(4005, "用户注册失败"),

    /**
     * 用户相关
     */
    REGISTER_FAIL(4101, "注册失败"),
    /**
     * 文章相关
     */
    CANNOT_DELETE(4201, "该目录还有子目录，不能直接删除"),
    ADD_ARTICLE_FAIL(4202, "添加文章失败"),
    DELETE_ARTICLE_FAIL(4203, "删除文章失败"),
    ARTICLE_NOT_EXIST(4204, "文章不存在"),


    ;
    @Getter
    private int code;
    @Getter
    private String msg;
}

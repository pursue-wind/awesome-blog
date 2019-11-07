package cn.mirrorming.blog.domain.constants;

import lombok.Getter;

/**
 * 返回码枚举类
 */
@Getter
public enum CommonCode {

    /*公共部分*/
    SUCCESS("success", 200),  // 成功
    PARAMETER_NOT_ENOUGH("parameterNotEnough", 401),  // 参数不足
    FILE_EXPORT_FAILURE("fileExportFailure", 402),  // 文件导出失败
    RESULT_IS_NULL("resultIsNull", 403),  // 结果为空
    FILE_UPLOAD_FAILURE("fileUploadFailure", 404),  // 文件上传失败
    FILE_DOWNLOAD_FAILURE("fileDownLoadFailure", 405),  // 文件下载失败
    FILE_FORMAT_ERROR("fileFormatError", 407),  // 文件格式不对
    SIGN_ERROR("signError", 408),  // 签名不正确
    PAGE_SIZE_TOO_LARGE("pageSizeTooLarge", 409),  // 分页过大
    DB_SQL_ERROR("dbSqlFault", 410),  // sql错误
    PARAMETER_ERROR("parameterFault", 411),  // 参数错误

    SEVER_ERROR("systemFault", 500),  // 系统错误

    //    /*运营平台*/
//    CODE_LENGTH_ERROR("简码长度错误", 520),
//    CODE_PATTERN_ERROR("简码格式错误", 521),
//
//    /*权限相关*/
//    RE_LOGIN("登陆异常，需要重新登陆", 540),
    NO_PRIVILEGE("noPrivilege", 541);
//    SUCCESS_NO_DATA("数据为空", 542),
//    USER_FORBIDDEN("账户已禁用", 543),
//    USER_NOT_EXIST("账户不存在", 544),
//    USER_OR_PASSWORD_ERROR("账户或密码不正确", 545),
//
//    /*用户模块*/
//    USERNAME_EXIST_ERROR("用户名或邮箱已经存在", 501),
//    TEL_BIND_ERROR("手机号码已经绑定了其它用户名", 502),
//    OPENID_NOT_EXIST_ERROR("两次密码输入不正确", 503),
//    USER_AUTHOR_ERROR("账号未激活", 504),
//
//
//    /*交易相关*/
//    INVALID_AMOUNT("无效的金额",421),
//    NOTENOUGH("余额不足", 422),

    private String message;
    private int code;

    CommonCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public static String getMessageByCode(int code) {
        for (CommonCode enuma : CommonCode.values()) {
            if (enuma.getCode() == code) {
                return enuma.getMessage();
            }
        }
        return "未知枚举项";
    }
}

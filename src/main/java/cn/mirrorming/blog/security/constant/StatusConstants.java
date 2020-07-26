package cn.mirrorming.blog.security.constant;

/**
 * @author Mireal
 */
public interface StatusConstants {
    /** 请求成功*/
    int OK = 200;
    /** 失败*/
    int FAIL = -500;
    /** 未认证*/
    int NOT_AUTHENTICATED = -10000;
    /** 认证错误*/
    int BAD_CREDENTIAL = -10001;
    /** 证书已失效*/
    int CREDENTIAL_INVALID = -10002;
    /** 无权限*/
    int NO_AUTHORITY = -10003;
    /** 非法操作*/
    int BAD_OPERATION = -10004;
    /** 服务器错误*/
    int SERVER_ERROR = -10005;
    /** 证书已过期*/
    int CREDENTIAL_EXPIRED = -10006;
    /** 错误的状态*/
    int BAD_STATUS = 10001;
    /** 错误的数据类型*/
    int BAD_DATA_TYPE = 10002;
    /** 没有相关资源*/
    int NO_DATA = 10003;
}

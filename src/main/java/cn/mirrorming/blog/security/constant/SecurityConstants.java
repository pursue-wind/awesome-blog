/**
 *
 */
package cn.mirrorming.blog.security.constant;

/**
 * 通用常数
 *
 * @author Mireal
 */
public interface SecurityConstants {

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/validateCode";
    /**
     * 默认登出url
     */
    String DEFAULT_LOGOUT_URL = "/authenticated/logout";
    /**
     * 默认的用户名密码登录请求处理url
     */
    String DEFAULT_SIGN_IN_PROCESSING_URL_FORM = "/authentication/form";
    /**
     * 默认的手机验证码登录请求处理url
     */
    String DEFAULT_SIGN_IN_PROCESSING_URL_SMS = "/authentication/sms";
    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";
    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数名称
     */
    String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数名称
     */
    String DEFAULT_PARAMETER_NAME_USERNAME = "username";
    /**
     * 发送短信验证码 或 验证短信验证码时，传递的业务场景的参数名称
     */
    String DEFAULT_PARAMETER_NAME_SCENE = "scene";
    /**
     * 客户端ID唯一认证标识
     */
    String DEFAULT_PARAMETER_NAME_CLIENTID = "clientId";
    /**
     * 默认短信验证场景
     */
    String DEFAULT_VALIDATE_SCENE = "login";
    /**
     * 需要验证请求手机号是否是当前用户手机号
     */
    String CURRENT_AUTH_SCENE = "currentAuth";
}

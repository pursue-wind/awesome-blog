/**
 *
 */
package cn.mirrorming.blog.security.support.validate.sms;

/**
 *
 * @author Mireal

 */
public interface SmsCodeSender {

    /**
     *
     * @param mobile
     * @param smsScene
     * @param code
     */
    void sendCode(String mobile, String smsScene, String code);

}

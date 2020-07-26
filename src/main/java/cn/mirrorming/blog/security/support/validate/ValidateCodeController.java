package cn.mirrorming.blog.security.support.validate;

import cn.mirrorming.blog.security.constant.SecurityConstants;
import cn.mirrorming.blog.security.constant.CommonConstants;
import cn.mirrorming.blog.security.support.manager.ValidateCodeProcessor;
import cn.mirrorming.blog.security.support.validate.sms.SmsSendPermission;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

/**
 * 生成校验码的请求处理器
 *
 * @author Mireal
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ValidateCodeController {
    private static final String INTERVAL_KEY_PREFIX = "sms:auth:interval:";

    private final ValidateCodeProcessor smsCodeProcessor;
    private final ValidateCodeProcessor imageCodeProcessor;
    private final SmsSendPermission smsSendPermission;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/image")
    public void createCode(HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        String mobile = request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE);
        sendPermission(mobile, SecurityConstants.DEFAULT_VALIDATE_SCENE);

        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        servletWebRequestEdit(webRequest, SecurityConstants.DEFAULT_VALIDATE_SCENE);
        imageCodeProcessor.create(new ServletWebRequest(request, response));
    }

    /**
     * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
     *
     * @param request
     * @param response
     * @param scene
     * @throws Exception
     */
    @PostMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/sms/{scene}")
    public void createCodeByScene(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @PathVariable String scene) throws Exception {
        String mobile = request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_USERNAME);
        sendPermission(mobile, scene);
        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        servletWebRequestEdit(webRequest, scene);
        smsCodeProcessor.create(webRequest);
    }

    /**
     * 验证请求手机号是否可以发送短信
     *
     * @param mobile
     */
    private void sendPermission(String mobile, String scene) {
        if (StringUtils.isBlank(mobile) && !Pattern.matches(CommonConstants.DEFAULT_MOBILE_REGEX, mobile)) {
            throw new IllegalArgumentException("手机号码格式不正确");
        }

        smsSendPermission.sceneCheck(mobile, scene);
        smsSendPermission.sendInterval(mobile, INTERVAL_KEY_PREFIX);
    }

    private void servletWebRequestEdit(ServletWebRequest webRequest, String scene) {
        webRequest.setAttribute(SecurityConstants.DEFAULT_PARAMETER_NAME_SCENE, scene, RequestAttributes.SCOPE_REQUEST);
    }

}

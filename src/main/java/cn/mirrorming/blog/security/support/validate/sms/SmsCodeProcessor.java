package cn.mirrorming.blog.security.support.validate.sms;

import cn.mirrorming.blog.security.constant.SecurityConstants;
import cn.mirrorming.blog.security.support.validate.ValidateCode;
import cn.mirrorming.blog.security.support.validate.ValidateCodeGenerator;
import cn.mirrorming.blog.security.support.validate.ValidateCodeType;
import cn.mirrorming.blog.security.support.validate.impl.AbstractValidateCodeProcessor;
import cn.mirrorming.blog.domain.common.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 短信验证码处理器
 *
 * @author Mireal
 */
@Component
public class SmsCodeProcessor extends AbstractValidateCodeProcessor {

    /**
     * 短信验证码发送器
     */
    @Autowired
    private SmsCodeSender smsCodeSender;

    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    private String mobileParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_USERNAME;

    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), mobileParameter);
        String scene = String.valueOf(request.getAttribute(SecurityConstants.DEFAULT_PARAMETER_NAME_SCENE, RequestAttributes.SCOPE_REQUEST));

        smsCodeSender.sendCode(mobile, scene, validateCode.getCode());
        HttpServletResponse response = request.getResponse();
        assert response != null;
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(R.succeed()));
    }

    @Override
    protected ValidateCodeType getValidateCodeType() {
        return ValidateCodeType.sms;
    }

    @Override
    protected ValidateCode generate(ServletWebRequest request) {
        return smsCodeGenerator.generate(request);
    }
}

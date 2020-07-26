package cn.mirrorming.blog.security.support.validate.impl;

import cn.mirrorming.blog.security.support.validate.*;
import cn.mirrorming.blog.security.support.manager.ValidateCodeProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 抽象的验证码处理器
 *
 * @author Mireal
 */
public abstract class AbstractValidateCodeProcessor implements ValidateCodeProcessor {

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        ValidateCode validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    /**
     * 生成校验码
     *
     * @param request
     * @return
     */
    protected abstract ValidateCode generate(ServletWebRequest request);

    /**
     * 保存校验码
     *
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, ValidateCode validateCode) {
        // ValidateCode code = new ValidateCode(validateCode.getCode(),
        // validateCode.getExpireTime());
        validateCodeRepository.save(request, validateCode, getValidateCodeType());
    }

    /**
     * 发送校验码，由子类实现
     *
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, ValidateCode validateCode) throws Exception;

    /**
     * 根据请求的url获取校验码的类型
     *
     * @return
     */
    protected abstract ValidateCodeType getValidateCodeType();

    @Override
    public void validate(ServletWebRequest request) throws ValidateCodeException {

        ValidateCodeType codeType = getValidateCodeType();

        ValidateCode codeInCache = validateCodeRepository.get(request, codeType);

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeType.getParamName());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInCache == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInCache.isExpired()) {
            validateCodeRepository.remove(request, codeType);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInCache.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        validateCodeRepository.remove(request, codeType);
    }
}

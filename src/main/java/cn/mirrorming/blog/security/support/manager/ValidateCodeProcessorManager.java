package cn.mirrorming.blog.security.support.manager;

import cn.mirrorming.blog.security.support.validate.ValidateCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 校验码处理器管理器
 *
 * @author Mireal
 */
@Component
public class ValidateCodeProcessorManager {

    private final String beanSuffix = "CodeProcessor";

    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessorMap;

    /**
     * @param type
     * @return
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        String name = type.toString() + beanSuffix;
        ValidateCodeProcessor processor = validateCodeProcessorMap.get(name);
        Optional.ofNullable(processor).orElseThrow(() -> new AuthenticationServiceException("验证码处理器" + name + "不存在"));
        return processor;
    }

}

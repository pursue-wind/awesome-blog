/**
 *
 */
package cn.mirrorming.blog.security.support.validate.impl;

import cn.mirrorming.blog.security.constant.SecurityConstants;
import cn.mirrorming.blog.security.support.validate.ValidateCode;
import cn.mirrorming.blog.security.support.validate.ValidateCodeRepository;
import cn.mirrorming.blog.security.support.validate.ValidateCodeType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis的验证码存取器
 *
 * @author Mireal
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisValidateCodeRepository implements ValidateCodeRepository {
    private static final String KEY_PREFIX = "validateCode";
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
        try {
            redisTemplate.opsForValue().set(buildKey(request, type), objectMapper.writeValueAsString(code), 600, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new AuthenticationServiceException("不能序列化的验证码数据类型");
        }
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
        String value = redisTemplate.opsForValue().get(buildKey(request, type));
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(value, ValidateCode.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("不能逆序列化的验证码数据类型");
        }
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType type) {
        redisTemplate.delete(buildKey(request, type));
    }

    private String buildKey(ServletWebRequest request, ValidateCodeType type) {
        String deviceId = request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_CLIENTID);
        if (StringUtils.isBlank(deviceId)) {
            deviceId = request.getParameter(SecurityConstants.DEFAULT_PARAMETER_NAME_USERNAME);
            if (StringUtils.isBlank(deviceId)) {
                throw new AuthenticationServiceException("请在请求中携带必要参数");
            }
        }
        String scene = (String) request.getAttribute(SecurityConstants.DEFAULT_PARAMETER_NAME_SCENE, RequestAttributes.SCOPE_REQUEST);
        if (StringUtils.isBlank(scene)) {
            throw new AuthenticationServiceException("请在请求中携带场景信息");
        }
        return StringUtils.join(KEY_PREFIX, ":", type.toString(), ":", scene, ":", deviceId);
    }
}

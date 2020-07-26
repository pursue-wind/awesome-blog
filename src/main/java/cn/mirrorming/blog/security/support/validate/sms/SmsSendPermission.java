package cn.mirrorming.blog.security.support.validate.sms;

import cn.mirrorming.blog.domain.po.Users;
import cn.mirrorming.blog.mapper.generate.UsersMapper;
import cn.mirrorming.blog.security.constant.SecurityConstants;
import cn.mirrorming.blog.security.properties.CommonProperties;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 短信验证码场景检测器
 *
 * @author Mireal
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class SmsSendPermission {

    private final CommonProperties commonProperties;
    private final StringRedisTemplate redisTemplate;
    private final UsersMapper usersMapper;

    private static final long SEND_INTERVAL = 59L;

    private static final int SEND_MAX_SUM = 240;

    private static final int RESET_TIME_HOUR = 9;

    public void sceneCheck(String mobile, String scene) {
        if (!commonProperties.getSmsScene().containsKey(scene)) {
            throw new AuthenticationServiceException("不支持的短信验证场景:" + scene);
        }

        boolean registered = commonProperties.getRegisteredScene().get(scene);
        boolean mobileExist = usersMapper.selectOne(Wrappers.<Users>lambdaQuery().eq(Users::getPhone, mobile)) != null;
        if (registered ^ mobileExist) {
            // 如果是注册场景，并且手机号已存在，则抛出异常
            throw new AuthenticationServiceException(StringUtils.join("手机号", mobile, mobileExist ? "已注册" : "未注册"));
        }

        if (StringUtils.equals(scene, SecurityConstants.CURRENT_AUTH_SCENE) &&
                !StringUtils.equals(mobile, SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AuthenticationServiceException("登录信息异常或未登录");
        }
    }

    /**
     * 验证今日是否达到最大信息发送次数
     *
     * @return
     */
    public void sendMax(String mobile, String keyPrefix) {
        String countKey = buildKey(mobile, keyPrefix);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String sendSumStr = valueOperations.get(countKey);
        if (StringUtils.isNotBlank(sendSumStr) && Pattern.matches("[0-9]*", sendSumStr)) {
            Integer sendSum = Integer.valueOf(sendSumStr);
            if (sendSum < SEND_MAX_SUM) {
                redisTemplate.boundValueOps(countKey).increment(1);
            } else {
                throw new AuthenticationServiceException(StringUtils.join("手机号", mobile, "已达到最大发送次数"));
            }
        } else {
            // 计算到明天重置时间还剩下多久
            DateTime tomorrow = DateTime.now().plusDays(1).withHourOfDay(RESET_TIME_HOUR);
            int seconds = Seconds.secondsBetween(DateTime.now(), tomorrow).getSeconds();
            valueOperations.set(countKey, "1", seconds, TimeUnit.SECONDS);
        }
    }

    /***
     * 验证是否在信息发送间隔时间内**
     *
     * @return
     */
    public void sendInterval(String mobile, String keyPrefix) {
        String intervalKey = buildKey(mobile, keyPrefix);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (StringUtils.isEmpty(valueOperations.get(intervalKey))) {
            valueOperations.set(intervalKey, mobile, Duration.ofSeconds(SEND_INTERVAL));
        } else {
            throw new AuthenticationServiceException(StringUtils.join("手机号", mobile, "请求过于频繁"));
        }
    }

    private final String buildKey(String mobile, String keyPrefix) {
        return StringUtils.isNotBlank(mobile) ? keyPrefix + mobile : "";
    }
}

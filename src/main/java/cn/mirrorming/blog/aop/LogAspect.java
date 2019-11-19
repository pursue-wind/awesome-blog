package cn.mirrorming.blog.aop;

import cn.mirrorming.blog.aop.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * @author: mirror
 * @see LogRecord
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    private static final String LOG_RECORD_DEFAULT_VALUE = "default";

    @Pointcut("@annotation(cn.mirrorming.blog.aop.annotation.LogRecord)")
    public void dealLogPoint() {
    }

    @Around(value = "dealLogPoint()")
    public Object dealLogService(ProceedingJoinPoint point) throws Throwable {
        Object proceed = null;
        try {
            Method method = getMethod(point);
            // 获取注解对象
            LogRecord logRecord = method.getAnnotation(LogRecord.class);
            String val = logRecord.value();
            //所有参数
            Object[] args = point.getArgs();

            //日志记录
            if (logRecord.logOperation() == LogRecord.LogOperation.IN || logRecord.logOperation() == LogRecord.LogOperation.INOUT) {
                processInLog(val, method, args);
            }
            //执行业务
            proceed = point.proceed();
            if (logRecord.logOperation() == LogRecord.LogOperation.OUT || logRecord.logOperation() == LogRecord.LogOperation.INOUT) {
                processOutLog(val, method, proceed);
            }
        } catch (Exception e) {
            log.error("dealLogService error,JoinPoint:{}", point.getSignature(), e);
        }
        return proceed;
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    private void processInLog(String val, Method method, Object[] args) {
        if (StringUtils.equals(val, LOG_RECORD_DEFAULT_VALUE)) {
            log.info(" InLog == {} ==>  {}", method.getName(), args);
        } else {
            log.info(" InLog == {}: {} ==>  {}", val, method.getName(), args);
        }
    }

    private void processOutLog(String val, Method method, Object proceed) {
        if (StringUtils.equals(val, LOG_RECORD_DEFAULT_VALUE)) {
            log.info(" OutLog == {} ==>  {}", method.getName(), proceed);
        } else {
            log.info(" OutLog == {}: {} ==>  {}", val, method.getName(), proceed);
        }
    }
}

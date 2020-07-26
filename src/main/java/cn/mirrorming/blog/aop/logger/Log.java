package cn.mirrorming.blog.aop.logger;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <p>
 * 这个注解<tt>LogRecord</tt>是加在 controller 上用来记录日志的
 * <p>
 *
 * @author Mireal Chan
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 日志说明
     */
    @AliasFor("value")
    String desc() default "default";

    String value() default "default";

    LogOperation logOperation() default LogOperation.INOUT;

    /**
     * 日志打印类型
     */
    enum LogOperation {
        // 进来时打印
        IN,
        // 出去时打印
        OUT,
        // 都打印
        INOUT;
    }
}

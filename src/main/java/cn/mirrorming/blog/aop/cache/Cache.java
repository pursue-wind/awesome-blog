package cn.mirrorming.blog.aop.cache;

import java.lang.annotation.*;

/**
 * @author Mireal
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    String value();
}

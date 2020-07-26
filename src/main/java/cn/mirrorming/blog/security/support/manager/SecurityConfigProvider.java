package cn.mirrorming.blog.security.support.manager;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * @author Mireal
 */
@FunctionalInterface
public interface SecurityConfigProvider {
    void configure(HttpSecurity builder) throws Exception;
}

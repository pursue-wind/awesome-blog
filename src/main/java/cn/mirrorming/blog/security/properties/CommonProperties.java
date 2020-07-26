package cn.mirrorming.blog.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mireal
 */
@Data
@ConfigurationProperties("common")
public class CommonProperties {

    private Map<String, String> smsScene = new HashMap<>();

    private Map<String, Boolean> registeredScene = new HashMap<>();

}

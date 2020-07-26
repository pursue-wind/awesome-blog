package cn.mirrorming.blog.security.properties;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mireal
 */
@Data
public class Oauth2Properties {
    private List<OAuth2ClientProperties> clients = new LinkedList<>();
}

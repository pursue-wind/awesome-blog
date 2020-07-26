package cn.mirrorming.blog.security.properties;

import lombok.Data;

/**
 * @author Mireal
 */
@Data
public class ImageCodeProperties {

    private Integer length = 4;

    private Integer expireIn = 300;

    private String url;

    private int width = 68;

    private int height = 24;

}

/**
 *
 */
package cn.mirrorming.blog.config.properties;

import lombok.Data;

/**
 * 社交登录配置项
 * @author mireal
 *
 */
@Data
public class SocialProperties {

    /**
     * 社交登录功能拦截的url
     */
    private String filterProcessesUrl = "/auth";

    /**
     * 创建一个qq配置对象
     */
    private QQProperties qq = new QQProperties();

//	private WeixinProperties weixin = new WeixinProperties();
}

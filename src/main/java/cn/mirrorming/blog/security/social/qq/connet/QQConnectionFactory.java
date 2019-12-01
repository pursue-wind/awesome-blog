/**
 *
 */
package cn.mirrorming.blog.security.social.qq.connet;

import cn.mirrorming.blog.security.social.qq.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/1 16:49
 * 泛型是API的类型
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    /**
     * providerId：服务提供商的唯一标志，通过配置文件配置
     * @param providerId
     * @param appId
     * @param appSecret
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }

}

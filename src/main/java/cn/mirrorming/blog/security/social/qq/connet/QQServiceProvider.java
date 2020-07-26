/**
 *
 */
package cn.mirrorming.blog.security.social.qq.connet;

import cn.mirrorming.blog.security.social.qq.QQ;
import cn.mirrorming.blog.security.social.qq.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/1 16:50
 *
 * 抽象类AbstractOAuth2ServiceProvider的泛型是指Api接口的实现类
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 流程中的第一步：导向认证服务器的url
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 第四步：申请令牌的url
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * 因为每个应用的AppId和AppSecret都不一样
     * @param appId
     * @param appSecret
     */
    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }


    @Override
    public QQ getApi(String accessToken) {
        // QQImpl必须是多例的，不能把QQImpl声明为@Component组件
        // accessToken抽象类会直接传进来
        return new QQImpl(accessToken, appId);
    }
}

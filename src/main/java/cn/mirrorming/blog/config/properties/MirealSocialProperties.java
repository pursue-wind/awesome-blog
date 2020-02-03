package cn.mirrorming.blog.config.properties;

import lombok.Data;

/**
 * @author Mireal
 */
@Data
public class MirealSocialProperties {

    /**
     * 社交登录拦截路径
     */
    private String filterProcessesUrl = "/auth";

    private QQProperties qq = new QQProperties();

    private GithubProperties github = new GithubProperties();

    private GiteeProperties gitee = new GiteeProperties();
}

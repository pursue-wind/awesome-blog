package cn.mirrorming.blog.security.social.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 继承默认的社交登录配置，加入自定义的后处理逻辑，此处用于修改{@link SocialAuthenticationFilter}过滤器默认拦截的请求路径
 *
 * @author mireal
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MirealSpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessesUrl;

    private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

    public MirealSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        // SocialAuthenticationFilter过滤器默认拦截的请求是/auth开头，这里修改为自己配置的
        filter.setFilterProcessesUrl(filterProcessesUrl);
        if (socialAuthenticationFilterPostProcessor != null) {
            socialAuthenticationFilterPostProcessor.process(filter);
        }
        return (T) filter;
    }
}

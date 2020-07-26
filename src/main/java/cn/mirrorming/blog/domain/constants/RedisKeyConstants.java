package cn.mirrorming.blog.domain.constants;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2020/3/24 11:43
 */
public class RedisKeyConstants {
    /**
     * Redis 过期监听 key
     */
    public static final String ARTICLE_CLICK_EXPIRED_PREFIX = "ARTICLE_CLICK_EXPIRED::";
    public static final String ARTICLE_CLICK_PREFIX = "ARTICLE_CLICK::";
}

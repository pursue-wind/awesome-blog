package cn.mirrorming.blog.event.redis;

import cn.mirrorming.blog.utils.RedisOperator;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.mapper.generate.ArticleMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import static cn.mirrorming.blog.domain.constants.RedisKeyConstants.ARTICLE_CLICK_EXPIRED_PREFIX;
import static cn.mirrorming.blog.domain.constants.RedisKeyConstants.ARTICLE_CLICK_PREFIX;

/**
 * @author Mireal Chan
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {


    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private ArticleMapper articleMapper;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取失效的key
        String expiredKey = message.toString();

        // 点击统计过期
        if (expiredKey.startsWith(ARTICLE_CLICK_EXPIRED_PREFIX)) {
            String s = expiredKey.split(ARTICLE_CLICK_EXPIRED_PREFIX)[1];
            Integer articleId = Integer.valueOf(s);
            String timesStr = redisOperator.get(ARTICLE_CLICK_PREFIX + articleId);
            if (null == timesStr) {
                return;
            }
            Integer articleClickTimes = Integer.valueOf(timesStr);
            UpdateWrapper<Article> wrapper = Wrappers.<Article>update().setSql("click = click + " + articleClickTimes);
            // Key 失效时，更新文章点击数
            articleMapper.update(new Article(), wrapper.lambda().eq(Article::getId, articleId));
            redisOperator.delete(ARTICLE_CLICK_PREFIX + articleId);
        }
//            String s = expiredKey.split(REDIS_UID_PRE)[1];
//            Integer userId = Integer.valueOf(s);
////            Object timeObj = redisOperator.get(REDIS_REQUEST_TIME_PRE + userId);
////            Optional.ofNullable(timeObj);
//
//            Date secTimeAgo = JodaTimeUtils.getSecTimeAgo(-300);
//            userService.updateLastLoginTime(userId, secTimeAgo);
    }
}
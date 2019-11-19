package cn.mirrorming.blog.event;


import cn.mirrorming.blog.mapper.auto.ArticleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/19 20:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleClickEventListener {
    private final ArticleMapper articleMapper;
    private static final ConcurrentHashMap<Integer, Integer> MAP = new ConcurrentHashMap();
    private static final Integer TEN_TIMES = 10;


    @EventListener
    @Async
    public void articleClick(ArticleClickEvent event) {
        log.info("[event] 文章点击 ==> [{}]", event.toString());
        Integer id = event.getId();
        String readPassword = event.getReadPassword();
        MAP.compute(id, (k, v) -> v == null ? 1 : ++v);
        if (MAP.containsValue(TEN_TIMES)) {
            //articleMapper.u()
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        Map<Integer, Integer> myMap = new HashMap<>();


        myMap.compute(1, (k, v) -> v == null ? 1 : ++v);
        myMap.compute(1, (k, v) -> ++v);

    }

}

package cn.mirrorming.blog.utils;

import cn.mirrorming.blog.domain.po.Article;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2020/2/27 10:40
 */
@UtilityClass
public class MyCollectionUtils {
    public Map<String, List> transformKey(Map<Integer, List<Article>> map, String suffixVal) {
        Function<Integer, String> transformMapKey2String = key -> key + suffixVal;
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(transformMapKey2String.compose(Map.Entry::getKey), Map.Entry::getValue));
    }

    public Function<Map, Map> transformKey2(Map<Integer, List> map, String suffixVal) {
        Function<Integer, String> transformMapKey2String = key -> key + suffixVal;
        Map<String, List> collect = map.entrySet()
                .stream()
                .collect(Collectors.toMap(transformMapKey2String.compose(Map.Entry::getKey), Map.Entry::getValue));
        return map1 -> collect;
    }
}

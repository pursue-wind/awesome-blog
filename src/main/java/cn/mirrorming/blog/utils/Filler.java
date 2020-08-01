package cn.mirrorming.blog.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Mireal
 * @version V1.0
 */
public class Filler {
    static class FillerImpl<IN, R> {
        void fill(Supplier<Collection<IN>> data,
                  Function<IN, Integer> getter,
                  BiConsumer<IN, R> setter,
                  Function<Collection<Integer>, Map<Integer, R>> function) {
            Collection<IN> collection = data.get();
            Collection<Integer> ids = collection.stream().map(getter).collect(Collectors.toSet());
            Map<Integer, R> map = function.apply(ids);
            collection.forEach(in -> setter.accept(in, map.get(getter.apply(in))));
        }
    }

    /**
     * 对象属性填充 IN { id, R(null) }   ->   IN { id, R(value) }
     *
     * @param data
     * @param getter    对象数据来源 eg：id / type
     * @param setter    对象设值字段
     * @param queryData 根据数据来源 转换值 id -> DB -> R
     * @param <IN>      输入
     * @param <R>       设置值的类型
     */
    public static <IN, R> void fill(Supplier<Collection<IN>> data,
                                    Function<IN, Integer> getter,
                                    BiConsumer<IN, R> setter,
                                    Function<Collection<Integer>, Map<Integer, R>> queryData) {
        new FillerImpl<IN, R>().fill(data, getter, setter, queryData);
    }

    /**
     * List -> Map
     */
    public static <K, V, T> Map<K, V> list2Map(List<T> list, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * List -> Map
     */
    public static <K, T> Map<K, T> list2Map(List<T> list, Function<T, K> keyMapper) {
        return list2Map(list, keyMapper, Function.identity());
    }
}

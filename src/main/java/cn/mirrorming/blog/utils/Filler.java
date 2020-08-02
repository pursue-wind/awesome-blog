package cn.mirrorming.blog.utils;

import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        void fill(Supplier<Collection<IN>> data,
                  Map<Function<IN, Integer>, BiConsumer<IN, R>> propertiesMap,
                  Function<Collection<Integer>, Map<Integer, R>> function) {
            Collection<IN> collection = data.get();

            Collection<Integer> ids = propertiesMap.keySet().stream()
                    .map(getter -> collection.stream().map(getter))
                    .flatMap(Stream::distinct)
                    .collect(Collectors.toSet());

            Map<Integer, R> map = function.apply(ids);

            propertiesMap.forEach((getter, setter) -> {
                collection.forEach(in -> setter.accept(in, map.get(getter.apply(in))));
            });
        }
    }

    /**
     * 对象属性填充 IN { id, R(null) }   ->   IN { id, R(value) }
     *
     * @param data
     * @param getter    对象数据来源 eg：id / type
     * @param setter    对象设值字段
     * @param queryData 根据数据来源 转换值 id -> R
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
     * 对象属性填充 IN { id, R(null) }   ->   IN { id, R(value) }
     *
     * @param data
     * @param queryData     根据数据来源 转换值 id -> DB -> R
     * @param propertiesMap 字段映射（getter -> 值来源，setter -> 对象设值字段）
     * @param <IN>          输入
     * @param <R>           设置值的类型
     */
    public static <IN, R> void fill(Supplier<Collection<IN>> data,
                                    Function<Collection<Integer>, Map<Integer, R>> queryData,
                                    Map<Function<IN, Integer>, BiConsumer<IN, R>> propertiesMap) {
        new FillerImpl<IN, R>().fill(data, propertiesMap, queryData);
    }

    /**
     * 对象属性填充 IN { id, R(null) }   ->   IN { id, R(value) }
     *
     * @param data
     * @param queryData 根据数据来源 转换值 id -> R
     * @param k1        getter1 -> 值来源
     * @param v1        setter1 -> 对象设值字段
     * @param k2        getter2 -> 值来源
     * @param v2        setter2 -> 对象设值字段
     * @param <IN>      输入
     * @param <R>       设置值的类型
     */
    public static <IN, R> void fill(Supplier<Collection<IN>> data,
                                    Function<Collection<Integer>, Map<Integer, R>> queryData,
                                    Function<IN, Integer> k1, BiConsumer<IN, R> v1,
                                    Function<IN, Integer> k2, BiConsumer<IN, R> v2) {
        new FillerImpl<IN, R>().fill(data, ImmutableMap.of(k1, v1, k2, v2), queryData);
    }

    /**
     * 对象属性填充 IN { id, R(null) }   ->   IN { id, R(value) }
     *
     * @param data
     * @param queryData 根据数据来源 转换值 id -> R
     * @param k1        getter1 -> 值来源 1
     * @param v1        setter1 -> 对象设值字段 1
     * @param k2        getter2 -> 值来源 2
     * @param v2        setter2 -> 对象设值字段 2
     * @param k3        getter3 -> 值来源 3
     * @param v3        setter3 -> 对象设值字段 3
     * @param <IN>      输入
     * @param <R>       设置值的类型
     */
    public static <IN, R> void fill(Supplier<Collection<IN>> data,
                                    Function<Collection<Integer>, Map<Integer, R>> queryData,
                                    Function<IN, Integer> k1, BiConsumer<IN, R> v1,
                                    Function<IN, Integer> k2, BiConsumer<IN, R> v2,
                                    Function<IN, Integer> k3, BiConsumer<IN, R> v3) {
        new FillerImpl<IN, R>().fill(data, ImmutableMap.of(k1, v1, k2, v2, k3, v3), queryData);
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

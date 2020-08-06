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

        /**
         * @param data     源
         * @param getter   DATA_getter
         * @param setter   DATA_setter
         * @param function DATA_provider
         */
        void fill(Supplier<Collection<IN>> data,
                  Function<IN, Integer> getter,
                  BiConsumer<IN, R> setter,
                  Function<Collection<Integer>, Map<Integer, R>> function) {
            Collection<IN> collection = data.get();
            Collection<Integer> ids = collection.stream().map(getter).filter(Objects::nonNull).collect(Collectors.toSet());
            Map<Integer, R> map = function.apply(ids);
            collection.forEach(in -> Optional.ofNullable(map.get(getter.apply(in))).ifPresent(r -> setter.accept(in, r)));
        }

        /**
         * @param data          源
         * @param propertiesMap DATA_getter:DATA_setter
         * @param function      DATA_provider
         */
        void fill(Supplier<Collection<IN>> data,
                  Map<Function<IN, Integer>, BiConsumer<IN, R>> propertiesMap,
                  Function<Collection<Integer>, Map<Integer, R>> function) {
            Collection<IN> collection = data.get();

            Collection<Integer> ids = propertiesMap.keySet().stream()
                    .map(getter -> collection.stream().map(getter).filter(Objects::nonNull))
                    .flatMap(Stream::distinct)
                    .collect(Collectors.toSet());
            Map<Integer, R> map = function.apply(ids);
            propertiesMap.forEach((getter, setter) -> collection.forEach(in -> Optional.ofNullable(map.get(getter.apply(in))).ifPresent(r -> setter.accept(in, r))));
        }

        /**
         * @param data          源
         * @param propertiesMap DATA_getter:{R_getter:R_setter}
         * @param provider      DATA_provider
         */
        void fillMultiValue(Supplier<Collection<IN>> data,
                            Map<Function<IN, Integer>, Map<Function<R, String>, BiConsumer<IN, String>>> propertiesMap,
                            Function<Collection<Integer>, Map<Integer, R>> provider) {
            Collection<IN> collection = data.get();

            Collection<Integer> ids = propertiesMap.keySet().stream()
                    .map(getter -> collection.stream().map(getter).filter(Objects::nonNull))
                    .flatMap(Stream::distinct)
                    .collect(Collectors.toSet());

            Map<Integer, R> map = provider.apply(ids);
            propertiesMap.forEach(
                    (getter, setters) -> collection.forEach(in -> setters.forEach(
                            (rGetter, rSetter) -> Optional.ofNullable(map.get(getter.apply(in))).ifPresent(r -> rSetter.accept(in, rGetter.apply(r))))));
        }

        /**
         * @param data        源
         * @param providerMap DATA_getter:DATA_provider
         * @param setter      Setter
         */
        void fillOne(Supplier<Collection<IN>> data,
                     BiConsumer<IN, R> setter,
                     Map<Function<IN, Integer>, Function<Collection<Integer>, Map<Integer, R>>> providerMap) {
            Collection<IN> collection = data.get();
            Map<Function<IN, Integer>, Map<Integer, R>> getterMap = providerMap.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, map -> map.getValue().apply(collection.stream().map(map.getKey()).collect(Collectors.toSet()))));
            collection.forEach(in -> getterMap.forEach((getter, val) -> Optional.ofNullable(val.get(getter.apply(in))).ifPresent(r -> setter.accept(in, r))));
//            getterMap.forEach((getter, val) -> collection.forEach(in -> Optional.ofNullable(val.get(getter.apply(in))).ifPresent(r -> setter.accept(in, r))));
        }
    }

    /**
     * @param data        源
     * @param setter      setter
     * @param providerMap getter:DATA_provider
     */
    public static <IN, R> void fillOne(Supplier<Collection<IN>> data,
                                       BiConsumer<IN, R> setter,
                                       Map<Function<IN, Integer>, Function<Collection<Integer>, Map<Integer, R>>> providerMap) {
        new FillerImpl<IN, R>().fillOne(data, setter, providerMap);
    }

    /**
     * 填充多个值
     *
     * @param data     源
     * @param getter   getter
     * @param valueMap valueMapping
     * @param provider DATA_provider
     */
    public static <IN, R> void fillMultiValue(Supplier<Collection<IN>> data,
                                              Function<IN, Integer> getter,
                                              Map<Function<R, String>, BiConsumer<IN, String>> valueMap,
                                              Function<Collection<Integer>, Map<Integer, R>> provider) {
        new FillerImpl<IN, R>().fillMultiValue(data, ImmutableMap.of(getter, valueMap), provider);
    }


    /**
     * 填充多个值
     *
     * @param data
     * @param propertiesMap 属性映射map
     * @param function      数据源
     * @param <IN>          输入
     * @param <R>           设置值的类型
     */
    public static <IN, R> void fillMultiValue(Supplier<Collection<IN>> data,
                                              Map<Function<IN, Integer>, Map<Function<R, String>, BiConsumer<IN, String>>> propertiesMap,
                                              Function<Collection<Integer>, Map<Integer, R>> function) {
        new FillerImpl<IN, R>().fillMultiValue(data, propertiesMap, function);
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
    public static <IN, R> void fill(IN data,
                                    Function<Collection<Integer>, Map<Integer, R>> queryData,
                                    Map<Function<IN, Integer>, BiConsumer<IN, R>> propertiesMap) {
        new FillerImpl<IN, R>().fill(() -> Collections.<IN>singleton(data), propertiesMap, queryData);
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

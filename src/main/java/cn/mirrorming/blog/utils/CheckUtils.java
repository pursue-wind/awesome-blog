package cn.mirrorming.blog.utils;

import java.util.function.Supplier;


public class CheckUtils {

    /**
     * 不能为空
     */
    public static void mustNull(Object obj, Supplier<RuntimeException> exceptionSupplier) {
        if (obj != null) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 不能为空
     */
    public static void notNull(Object obj, Supplier<RuntimeException> exceptionSupplier) {
        if (obj == null) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 必须为真
     */
    public static void mustTrue(boolean value, Supplier<RuntimeException> exceptionSupplier) {
        if (!value) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 必须为假
     */
    public static void mustFalse(boolean value, Supplier<RuntimeException> exceptionSupplier) {
        if (value) {
            throw exceptionSupplier.get();
        }
    }
}

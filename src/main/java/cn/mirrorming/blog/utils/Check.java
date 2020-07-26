package cn.mirrorming.blog.utils;


import cn.mirrorming.blog.exception.AppException;

import java.util.function.Supplier;

/**
 * @author Mireal Chan
 */
public final class Check {

    private final boolean flag;

    private Check(boolean b) {
        this.flag = b;
    }

    public static Check ifCorrect(boolean b) {
        return new Check(b);
    }

    /**
     * 影响行数
     *
     * @param rows     实际删除行数
     * @param affected 期望影响行数
     * @return
     */
    public static Check affectedRows(int rows, int affected) {
        return new Check(rows == affected);
    }

    /**
     * 影响行数 = 1 -> true
     *
     * @param row 实际删除行数
     * @return
     */
    public static Check affectedOneRow(int row) {
        return new Check(row == 1);
    }


    public static Check checkNotNull(Object... objects) {
        for (Object o : objects) {
            if (null == o) {
                return new Check(true);
            }
        }
        return new Check(false);
    }

    public <X extends Throwable> boolean orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.flag) {
            return true;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public <X extends Throwable> boolean orElseThrowEx(X throwable) throws X {
        if (this.flag) {
            return true;
        } else {
            throw throwable;
        }
    }

    public static void main(String[] args) {
        Check.affectedOneRow(1).orElseThrow(() -> new AppException(""));
    }
}
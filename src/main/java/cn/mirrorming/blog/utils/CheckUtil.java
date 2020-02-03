package cn.mirrorming.blog.utils;

import cn.mirrorming.blog.exception.CheckException;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/25 19:28
 */
public class CheckUtil {
    public static void requireAllNonNull(String e, Object... objects) {
        for (Object o : objects) {
            if (null == o) {
                throw new CheckException(e);
            }
        }
    }

}

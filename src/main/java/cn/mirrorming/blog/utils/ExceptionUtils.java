package cn.mirrorming.blog.utils;

import cn.mirrorming.blog.domain.enums.RespEnum;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.exception.ArticleException;
import lombok.experimental.UtilityClass;

/**
 * @author Mireal
 * @version V1.0
 */
@UtilityClass
public class ExceptionUtils {
    /**
     * 抛出 AppException
     *
     * @param respEnum 响应枚举
     * @return
     */
    public AppException appEx(RespEnum respEnum) {
        return new AppException(respEnum);
    }

    /**
     * 抛出 AppException
     *
     * @param msg 错误消息
     * @return
     */
    public ArticleException articleEx(String msg) {
        return new ArticleException(msg);
    }
}

package cn.mirrorming.blog.domain.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回数据对象
 *
 * @author Mireal Chan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    /**
     * 查询数据列表
     */
    private T data;
    /**
     * 总数
     */
    private Long total;

    /**
     * 当前页
     */
    private Long current;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回码
     */
    private int statusCode;

    public static <T> PageDTO<T> succeed(T data, Long current, Long total) {
        return new PageDTO<>(data, total, current, "success", 200);
    }
}

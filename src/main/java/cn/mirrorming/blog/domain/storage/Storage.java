package cn.mirrorming.blog.domain.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * OS 文件相关属性
 *
 * @author mireal
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Storage implements Serializable {
    //对象KEY
    private String name;
    //对象名称
    private String type;
    //对象类型
    private long size;
    //对象大小
    private String url;
    //对象链接
    private String key;
}
package cn.mirrorming.blog.domain.base;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author Mireal
 */
@Data
public class BaseEntity {
//    private Integer id;
    private Date createTime;
    private Date updateTime;
}

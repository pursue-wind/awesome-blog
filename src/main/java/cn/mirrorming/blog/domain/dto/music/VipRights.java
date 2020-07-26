package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;

/**
 * @author Mireal Chan
 */
@Data
public class VipRights {
    private Associator associator;
    private String musicPackage;
    private Integer redVipAnnualCount;
}
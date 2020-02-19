package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author mireal
 */
@Data
@JsonIgnoreProperties({"vipRights", "authStatus", "liveInfo"})
public class CloudMusicUser {
    private String locationInfo;
    private String nickname;
    private VipRights vipRights;
    private String avatarUrl;
    private String experts;
    private String expertTags;
    private Integer authStatus;
    private Integer userType;
    private Integer vipType;
    private String remarkName;
    private long userId;
}
package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/9 13:08
 */
@Data
@JsonIgnoreProperties({"isMusician", "userId", "topComments", "moreHot", "hotComments", "more"})
public class NetEaseCommentDTO {
    private Boolean isMusician;
    private Integer userId;
    private List<String> topComments;
    private Boolean moreHot;
    private List<HotComments> hotComments;
    private Integer code;
    private List<Comments> comments;
    private Integer total;
    private Boolean more;

    @Data
    public class Associator {
        private Integer vipCode;
        private Boolean rights;
    }

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

    @Data
    @JsonIgnoreProperties({"decoration", "isRemoveHotComment", "beReplied", "pendantData", "showFloorComment", "commentLocationType", "decoration", "repliedMark", "expressionUrl"})
    public class Comments {
        @JsonProperty("user")
        private CloudMusicUser cloudMusicUser;
        private List<String> beReplied;
        private String pendantData;
        private String showFloorComment;
        private Integer status;
        private Integer commentLocationType;
        private Integer parentCommentId;
        private Boolean repliedMark;
        private long commentId;
        private String expressionUrl;
        private Boolean liked;
        private long time;
        private Integer likedCount;
        private String content;
        private Boolean isRemoveHotComment;
    }

    @Data
    public class HotComments {
        private CloudMusicUser user;
        private List<String> beReplied;
        private String pendantData;
        private String showFloorComment;
        private Integer status;
        private Integer commentLocationType;
        private Integer parentCommentId;
        private String decoration;
        private Boolean repliedMark;
        private long commentId;
        private String expressionUrl;
        private Boolean liked;
        private long time;
        private Integer likedCount;
        private String content;
    }

    @Data
    public class VipRights {
        private Associator associator;
        private String musicPackage;
        private Integer redVipAnnualCount;
    }
}
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
public class NetEaseCommentDto {
    private boolean isMusician;
    private int userId;
    private List<String> topComments;
    private boolean moreHot;
    private List<HotComments> hotComments;
    private int code;
    private List<Comments> comments;
    private int total;
    private boolean more;

    @Data
    public static class HotComments {
        private CloudMusicUser user;
        private List<String> beReplied;
        private String pendantData;
        private String showFloorComment;
        private int status;
        private int commentLocationType;
        private int parentCommentId;
        private String decoration;
        private boolean repliedMark;
        private long commentId;
        private String expressionUrl;
        private boolean liked;
        private long time;
        private int likedCount;
        private String content;


    }

    @Data
    @JsonIgnoreProperties({"isRemoveHotComment", "beReplied", "pendantData", "showFloorComment", "commentLocationType", "decoration", "repliedMark", "expressionUrl"})
    public static class Comments {
        @JsonProperty("user")
        private CloudMusicUser cloudMusicUser;
        private List<String> beReplied;
        private String pendantData;
        private String showFloorComment;
        private int status;
        private int commentLocationType;
        private int parentCommentId;
        private Decoration decoration;
        private boolean repliedMark;
        private long commentId;
        private String expressionUrl;
        private boolean liked;
        private long time;
        private int likedCount;
        private String content;
        private boolean isRemoveHotComment;
    }

    @Data
    @JsonIgnoreProperties({"vipRights", "authStatus"})
    public static class CloudMusicUser {
        private String locationInfo;
        private String nickname;
        private VipRights vipRights;
        private String avatarUrl;
        private String experts;
        private String expertTags;
        private int authStatus;
        private int userType;
        private int vipType;
        private String remarkName;
        private long userId;
    }

    @Data
    public static class Decoration {

    }

    @Data
    public static class VipRights {
        private Associator associator;
        private String musicPackage;
        private int redVipAnnualCount;
    }

    @Data
    public static class Associator {
        private int vipCode;
        private boolean rights;
    }
}
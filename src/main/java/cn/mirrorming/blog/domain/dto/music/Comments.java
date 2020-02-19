package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mireal
 */
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
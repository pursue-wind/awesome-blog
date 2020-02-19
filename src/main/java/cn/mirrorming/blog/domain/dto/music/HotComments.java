package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;

import java.util.List;

/**
 * @author mireal
 */
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
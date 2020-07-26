package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author Mireal Chan
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
}
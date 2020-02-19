package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
/**
 * @author mireal
 */
@Data
@JsonIgnoreProperties({"id", "picId", "img1v1Id", "briefDesc", "picUrl", "img1v1Url", "albumSize", "alias", "trans", "musicSize", "topicPerson"})
public class Artists {
    private String name;
    private int id;
    private int picId;
    private int img1v1Id;
    private String briefDesc;
    private String picUrl;
    private String img1v1Url;
    private int albumSize;
    private List<String> alias;
    private String trans;
    private int topicPerson;
    private int musicSize;
}

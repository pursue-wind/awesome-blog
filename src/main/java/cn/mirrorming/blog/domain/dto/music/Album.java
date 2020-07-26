package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author Mireal Chan
 */
@Data
@JsonIgnoreProperties({"songs", "artists", "artist", "alias", "picId_str", "transName","mark"})
public class Album {
    private String name;
    private long id;
    private String type;
    private int size;
    private long picId;
    private String blurPicUrl;
    private int companyId;
    private long pic;
    private String picUrl;
    private long publishTime;
    private String description;
    private String tags;
    private String company;
    private String briefDesc;
    private List<String> songs;
    private List<String> alias;
    private int status;
    private int copyrightId;
    private String commentThreadId;
    private String subType;
    private String transName;
    private String picId_str;
}

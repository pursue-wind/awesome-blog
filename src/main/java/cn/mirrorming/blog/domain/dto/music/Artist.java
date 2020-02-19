package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;

import java.util.List;

/**
 * @author mireal
 */
@Data
public class Artist {
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
    private int musicSize;
}
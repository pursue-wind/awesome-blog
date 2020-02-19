package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author mireal
 */
@Data
@JsonIgnoreProperties({"alias", "hMusic", "mMusic", "lMusic", "bMusic", "disc", "hearTime", "ringtone", "transName", "transNames", "sign"})
public class Songs {
    private String name;
    private long id;
    private int position;
    private List<String> alias;
    private int status;
    private int fee;
    private int copyrightId;
    private String disc;
    private int no;
    private List<Artists> artists;
    private Album album;
    private boolean starred;
    private int popularity;
    private int score;
    private int starredNum;
    private long duration;
    private int playedNum;
    private int dayPlays;
    private int hearTime;
    private String ringtone;
    private String crbt;
    private String audition;
    private String copyFrom;
    private String commentThreadId;
    private String rtUrl;
    private int ftype;
    private List<String> rtUrls;
    private int copyright;
    private int rtype;
    private String rurl;
    private String mp3Url;
    private int mvid;
    private String transName;
    private String sign;
}

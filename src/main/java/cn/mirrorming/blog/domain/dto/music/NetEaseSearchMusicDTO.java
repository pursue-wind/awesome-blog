package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/9 13:08
 */
@Data
public class NetEaseSearchMusicDTO {
    private Result result;
    private int code;

    @Data
    private class Result {
        private List<Songs> songs;
        private int songCount;
    }

    @Data
    @JsonIgnoreProperties({"songs", "artists", "artist", "alias", "picId_str", "transName"})
    private class Album {
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

    @Data
    private class Artist {
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

    @Data
    @JsonIgnoreProperties({"id", "picId", "img1v1Id", "briefDesc", "picUrl", "img1v1Url", "albumSize", "alias", "trans", "musicSize", "topicPerson"})
    private class Artists {
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

    @Data
    private class BMusic {
        private String name;
        private Long id;
        private Long size;
        private String extension;
        private int sr;
        private Long dfsId;
        private Long bitrate;
        private Long playTime;
        private Double volumeDelta;
    }

    @Data
    private class HMusic {
        private String name;
        private Long id;
        private Long size;
        private String extension;
        private int sr;
        private Long dfsId;
        private Long bitrate;
        private Long playTime;
        @JsonIgnore
        private Double volumeDelta;
    }

    @Data
    private class LMusic {
        private String name;
        private Long id;
        private Long size;
        private String extension;
        private Integer sr;
        private Long dfsId;
        private Long bitrate;
        private Long playTime;
        private Double volumeDelta;
    }

    @Data
    private class MMusic {
        private String name;
        private long id;
        private long size;
        private String extension;
        private int sr;
        private long dfsId;
        private long bitrate;
        private long playTime;
        @JsonIgnore
        private String volumeDelta;
    }

    @Data
    @JsonIgnoreProperties({"alias", "hMusic", "mMusic", "lMusic", "bMusic", "disc", "hearTime", "ringtone", "transName", "transNames", "sign"})
    private class Songs {
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
}

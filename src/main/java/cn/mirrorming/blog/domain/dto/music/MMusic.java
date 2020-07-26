package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Mireal Chan
 */
@Data
public class MMusic {
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
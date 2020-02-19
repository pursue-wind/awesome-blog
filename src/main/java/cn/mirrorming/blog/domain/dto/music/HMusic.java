package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author mireal
 */
@Data
public class HMusic {
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

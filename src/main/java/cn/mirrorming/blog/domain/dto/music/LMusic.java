package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;

/**
 * @author mireal
 */
@Data
public class LMusic {
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
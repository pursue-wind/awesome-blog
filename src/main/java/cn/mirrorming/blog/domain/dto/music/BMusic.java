package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;

/**
 * @author Mireal Chan
 */
@Data
public class BMusic {
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

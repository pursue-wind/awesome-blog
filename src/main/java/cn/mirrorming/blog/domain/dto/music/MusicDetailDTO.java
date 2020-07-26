package cn.mirrorming.blog.domain.dto.music;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author Mireal Chan
 */
@Data
@JsonIgnoreProperties({"equalizers"})
public class MusicDetailDTO {
    private List<Songs> songs;
    private int code;
}
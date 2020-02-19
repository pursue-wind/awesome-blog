package cn.mirrorming.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author mireal
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicSearchResDTO implements Serializable {

    /**
     * 网易云音乐id
     */
    private String musicId;

    /**
     * 歌曲名称
     */
    private String name;

    /**
     * 歌手名称
     */
    private List<String> artists;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 歌曲图片链接
     */
    private String coverUrl;

    /**
     * 歌曲播放链接
     */
    private String url;

    /**
     * 歌词
     */
    private String lrcUrl;

    private static final long serialVersionUID = 1L;
}
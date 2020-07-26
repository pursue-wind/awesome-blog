package cn.mirrorming.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2020/3/3 19:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicSearchResult {
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
    private String artist;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 歌曲图片链接
     */
    private String cover;

    /**
     * 歌曲播放链接
     */
    private String url;

    /**
     * 歌词
     */
    private String lrc;
    /**
     * 歌曲评论id
     */
    private String commentId;
}

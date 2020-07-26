package cn.mirrorming.blog.domain.dto.music;

import lombok.Data;

/**
 * @description: 歌词返回类
 @author Mireal
 **/
@Data
public class LyricReturn {
    private int songStatus;
    private int lyricVersion;
    private String lyric;
    private int code;
}
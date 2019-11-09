package cn.mirrorming.blog.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "music")
public class Music implements Serializable {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 网易云音乐id
     */
    @TableField(value = "music_id")
    private String musicId;

    /**
     * 歌曲名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 歌手名称
     */
    @TableField(value = "artist")
    private String artist;

    /**
     * 专辑名称
     */
    @TableField(value = "album_name")
    private String albumName;

    /**
     * 歌曲图片链接
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 歌曲播放链接
     */
    @TableField(value = "url")
    private String url;

    /**
     * 歌曲评论id
     */
    @TableField(value = "comment_id")
    private String commentId;

    /**
     * 歌词
     */
    @TableField(value = "lrc")
    private String lrc;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 歌单id
     */
    @TableField(value = "music_list_id")
    private Integer musicListId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_MUSIC_ID = "music_id";

    public static final String COL_NAME = "name";

    public static final String COL_ARTIST = "artist";

    public static final String COL_ALBUM_NAME = "album_name";

    public static final String COL_COVER = "cover";

    public static final String COL_URL = "url";

    public static final String COL_COMMENT_ID = "comment_id";

    public static final String COL_LRC = "lrc";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_MUSIC_LIST_ID = "music_list_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static MusicBuilder builder() {
        return new MusicBuilder();
    }
}
package cn.mirrorming.blog.domain.po;

import cn.mirrorming.blog.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value = "cn-mirrorming-blog-domain-po-Music")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "music")
public class Music extends BaseEntity implements Serializable {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "自增id")
    private Integer id;

    /**
     * 网易云音乐id
     */
    @TableField(value = "music_id")
    @ApiModelProperty(value = "网易云音乐id")
    private String musicId;

    /**
     * 歌曲名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "歌曲名称")
    private String name;

    /**
     * 歌手名称
     */
    @TableField(value = "artist")
    @ApiModelProperty(value = "歌手名称")
    private String artist;

    /**
     * 专辑名称
     */
    @TableField(value = "album_name")
    @ApiModelProperty(value = "专辑名称")
    private String albumName;

    /**
     * 歌曲图片链接
     */
    @TableField(value = "cover")
    @ApiModelProperty(value = "歌曲图片链接")
    private String cover;

    /**
     * 歌曲播放链接
     */
    @TableField(value = "url")
    @ApiModelProperty(value = "歌曲播放链接")
    private String url;

    /**
     * 歌曲评论id
     */
    @TableField(value = "comment_id")
    @ApiModelProperty(value = "歌曲评论id")
    private String commentId;

    /**
     * 歌词
     */
    @TableField(value = "lrc")
    @ApiModelProperty(value = "歌词")
    private String lrc;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 歌单id
     */
    @TableField(value = "music_list_id")
    @ApiModelProperty(value = "歌单id")
    private Integer musicListId;

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
}
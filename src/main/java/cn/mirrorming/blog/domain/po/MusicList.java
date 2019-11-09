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
@TableName(value = "music_list")
public class MusicList implements Serializable {
    /**
     * 歌单表主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 歌单名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 歌单图片
     */
    @TableField(value = "pic")
    private String pic;

    /**
     * 歌单用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 歌单标签
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 歌单介绍
     */
    @TableField(value = "introduction")
    private String introduction;

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

    public static final String COL_NAME = "name";

    public static final String COL_PIC = "pic";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_TAG = "tag";

    public static final String COL_INTRODUCTION = "introduction";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}
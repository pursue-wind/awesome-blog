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

@ApiModel(value = "cn-mirrorming-blog-domain-po-MusicList")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "music_list")
public class MusicList extends BaseEntity implements Serializable {
    /**
     * 歌单表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "歌单表主键")
    private Integer id;

    /**
     * 歌单名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "歌单名称")
    private String name;

    /**
     * 歌单图片
     */
    @TableField(value = "pic")
    @ApiModelProperty(value = "歌单图片")
    private String pic;

    /**
     * 歌单用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "歌单用户id")
    private Integer userId;

    /**
     * 歌单标签
     */
    @TableField(value = "tag")
    @ApiModelProperty(value = "歌单标签")
    private String tag;

    /**
     * 歌单介绍
     */
    @TableField(value = "introduction")
    @ApiModelProperty(value = "歌单介绍")
    private String introduction;

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
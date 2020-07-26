package cn.mirrorming.blog.domain.po;

import cn.mirrorming.blog.domain.base.BaseEntity;
import cn.mirrorming.blog.domain.es.EsMovie;
import cn.mirrorming.blog.utils.JacksonUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(value = "cn-mirrorming-blog-domain-po-Movie")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "movie")
public class Movie extends BaseEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private Integer id;

    @TableField(value = "name")
    @ApiModelProperty(value = "")
    private String name;

    @TableField(value = "coverUrl")
    @ApiModelProperty(value = "")
    private String coverurl;

    @TableField(value = "screenshot")
    @ApiModelProperty(value = "")
    private String screenshot;

    @TableField(value = "translatedName")
    @ApiModelProperty(value = "")
    private String translatedname;

    @TableField(value = "title")
    @ApiModelProperty(value = "")
    private String title;

    @TableField(value = "year")
    @ApiModelProperty(value = "")
    private String year;

    @TableField(value = "origin")
    @ApiModelProperty(value = "")
    private String origin;

    @TableField(value = "category")
    @ApiModelProperty(value = "")
    private String category;

    @TableField(value = "releaseDate")
    @ApiModelProperty(value = "")
    private String releasedate;

    @TableField(value = "score")
    @ApiModelProperty(value = "")
    private String score;

    @TableField(value = "duration")
    @ApiModelProperty(value = "")
    private String duration;

    @TableField(value = "director")
    @ApiModelProperty(value = "")
    private String director;

    @TableField(value = "actor")
    @ApiModelProperty(value = "")
    private String actor;

    @TableField(value = "description")
    @ApiModelProperty(value = "")
    private String description;

    @TableField(value = "downloadUrl")
    @ApiModelProperty(value = "")
    private String downloadurl;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_COVERURL = "coverUrl";

    public static final String COL_SCREENSHOT = "screenshot";

    public static final String COL_TRANSLATEDNAME = "translatedName";

    public static final String COL_TITLE = "title";

    public static final String COL_YEAR = "year";

    public static final String COL_ORIGIN = "origin";

    public static final String COL_CATEGORY = "category";

    public static final String COL_RELEASEDATE = "releaseDate";

    public static final String COL_SCORE = "score";

    public static final String COL_DURATION = "duration";

    public static final String COL_DIRECTOR = "director";

    public static final String COL_ACTOR = "actor";

    public static final String COL_DESCRIPTION = "description";

    public static final String COL_DOWNLOADURL = "downloadUrl";

    public Movie(EsMovie movie) {
        this.id = movie.getId();
        this.name = movie.getName();
        this.coverurl = movie.getCoverUrl();
        this.screenshot = movie.getScreenshot();
        this.title = movie.getTitle();
        this.year = movie.getYear();
        this.origin = movie.getOrigin();
        this.releasedate = movie.getReleaseDate();
        this.score = movie.getScore();
        this.duration = movie.getDuration();
        this.director = movie.getDirector();
        this.description = movie.getDescription();
        if (null != movie.getTranslatedName() && movie.getTranslatedName().size() != 0) {
            this.translatedname = JacksonUtils.obj2json(movie.getTranslatedName());
        }
        if (movie.getCategory().size() != 0) {
            this.category = JacksonUtils.obj2json(movie.getCategory());
        }
        if (movie.getActor().size() != 0) {
            this.actor = JacksonUtils.obj2json(movie.getActor());
        }
        if (movie.getDownloadUrl().size() != 0) {
            this.downloadurl = JacksonUtils.obj2json(movie.getDownloadUrl());
        }
    }
}
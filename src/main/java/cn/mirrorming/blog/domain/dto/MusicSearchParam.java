package cn.mirrorming.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/9 13:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicSearchParam {
    /**
     * s        搜索的内容
     * offset  当前页码
     * limit 每页数量
     * type     搜索的类型:  歌曲 1 / 专辑 10 / 歌手 100 / 歌单 1000 / 用户 1002 / mv 1004 / 歌词 1006 / 主播电台 1009
     */
    @NotNull
    private String s;
    @NotNull
    private String offset;
    @NotNull
    private String limit;
    @NotNull
    private String type;
}

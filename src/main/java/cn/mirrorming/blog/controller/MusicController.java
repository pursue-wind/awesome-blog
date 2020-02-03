package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.dto.MusicSearchDto;
import cn.mirrorming.blog.domain.dto.base.ResultData;
import cn.mirrorming.blog.domain.dto.music.NetEaseCommentDTO;
import cn.mirrorming.blog.domain.dto.music.NetEaseSearchMusicDTO;
import cn.mirrorming.blog.service.MusicService;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/9 13:46
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RestController
@RequestMapping("music")
public class MusicController {

    private final MusicService musicService;

    /**
     * 网易云音乐搜索
     *
     * @param musicSearchDto {@link MusicSearchDto}
     * @return ResultData {@link NetEaseSearchMusicDTO}
     */
    @PostMapping("search")
    public ResultData musicSearch(@RequestBody MusicSearchDto musicSearchDto) throws Exception {
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put("s", musicSearchDto.getS())
                .put("offset", musicSearchDto.getOffset())
                .put("limit", musicSearchDto.getLimit())
                .put("type", musicSearchDto.getType())
                .build();
        NetEaseSearchMusicDTO musicSearchDTO = musicService.netEaseMusicSearch(params);
        return ResultData.succeed(musicSearchDTO);
    }

    /**
     * 获得网易云评论
     *
     * @param id       网易云评论id 格式类似：R_SO_4_516997458   其中516997458为歌曲id
     * @param pageSize 每页数量
     * @param pageNum  当前页
     */
    @GetMapping("/comment")
    public ResultData getMusicCommentById(String id, String pageSize, String pageNum) throws Exception {
        NetEaseCommentDTO commentDTO = musicService.getNetEaseMusicComment(id, pageSize, pageNum);
        return ResultData.succeed(commentDTO);
    }
    /**
     * 查找当前用户所有歌单
     */

    /**
     * 查找当前用户选择歌单的所有音乐
     */

    /**
     * 歌单添加音乐
     */
    public void addMusic() {

    }

}
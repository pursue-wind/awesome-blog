package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.dto.MusicSearchDto;
import cn.mirrorming.blog.domain.dto.base.ResultData;
import cn.mirrorming.blog.domain.dto.music.NetEaseCommentDTO;
import cn.mirrorming.blog.domain.dto.music.NetEaseSearchMusicDTO;
import cn.mirrorming.blog.domain.po.Music;
import cn.mirrorming.blog.domain.po.MusicList;
import cn.mirrorming.blog.service.MusicService;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/9 13:46
 */
@Api(tags = "音乐")
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
    @ApiOperation(value = "网易云音乐搜索")
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
    @ApiOperation(value = "获得网易云评论")
    @GetMapping("/comment")
    public ResultData getMusicCommentById(String id, String pageSize, String pageNum) throws Exception {
        NetEaseCommentDTO commentDTO = musicService.getNetEaseMusicComment(id, pageSize, pageNum);
        return ResultData.succeed(commentDTO);
    }

    /**
     * 查找当前用户所有歌单
     */
    @ApiOperation(value = "查找当前用户所有歌单")
    @GetMapping("/list")
    public ResultData<List<MusicList>> selectMusicListByUser(Integer userId) {
        return ResultData.succeed(musicService.addMusicListByUser(userId));
    }

    /**
     * 查找当前用户选择歌单的所有音乐
     */
    @ApiOperation(value = "查找当前用户选择歌单的所有音乐", notes = "前端播放器使用")
    @GetMapping("/list/music")
    public ResultData<List<Music>> selectMusicByUserAndMusicList(Integer userId, Integer musicListId) {
        return ResultData.succeed(musicService.selectMusicByUserAndMusicList(userId, musicListId));
    }

    /**
     * 添加歌单
     */
    @ApiOperation(value = "添加歌单")
    @PostMapping("/list/add")
    public ResultData addMusicList(MusicList musicList) {
        return ResultData.succeed(musicService.addMusicList(musicList));
    }

    /**
     * 更新歌单
     */
    @ApiOperation(value = "更新歌单")
    @PutMapping("/list/update")
    public ResultData updateMusicList(MusicList musicList) {
        return ResultData.succeed(musicService.updateMusicList(musicList));
    }

    /**
     * 歌单添加音乐
     */
    @ApiOperation(value = "歌单添加音乐")
    @PostMapping("/list/addMusic")
    public ResultData addMusicListByUser(Music music) {
        return ResultData.succeed(musicService.addMusicListByUser(music));
    }
}

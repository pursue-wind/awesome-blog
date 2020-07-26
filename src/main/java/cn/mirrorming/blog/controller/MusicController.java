package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.aop.logger.Log;
import cn.mirrorming.blog.domain.dto.MusicSearchParam;
import cn.mirrorming.blog.domain.dto.MusicSearchResDTO;
import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.dto.music.NetEaseCommentDTO;
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

import javax.validation.Valid;
import java.util.List;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/9 13:46
 */
@CrossOrigin(origins = "*", maxAge = 3600)
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
     * @param musicSearchParam {@link MusicSearchParam}
     * @return ResultData {@link MusicSearchResDTO}
     */
    @Log(value = "网易云音乐搜索")
    @ApiOperation(value = "网易云音乐搜索")
    @PostMapping("search")
    public R musicSearch(@Valid @RequestBody MusicSearchParam musicSearchParam) throws Exception {
        ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put("s", musicSearchParam.getS())
                .put("offset", musicSearchParam.getOffset())
                .put("limit", musicSearchParam.getLimit())
                .put("type", musicSearchParam.getType())
                .build();
        MusicSearchResDTO musicSearchResDTO = musicService.netEaseMusicSearch(params);
        return R.succeed(musicSearchResDTO);
    }

    /**
     * 获得网易云评论
     *
     * @param id       网易云评论id 格式类似：R_SO_4_516997458   其中516997458为歌曲id
     * @param pageSize 每页数量
     * @param pageNum  当前页
     */
    @Log(value = "获得网易云评论")
    @ApiOperation(value = "获得网易云评论")
    @GetMapping("/comment")
    public R getMusicCommentById(String id, String pageSize, String pageNum) throws Exception {
        NetEaseCommentDTO commentDTO = musicService.getNetEaseMusicComment(id, pageSize, pageNum);
        return R.succeed(commentDTO);
    }

    @Log(value = "查找当前用户所有歌单", logOperation = Log.LogOperation.IN)
    @ApiOperation(value = "查找当前用户所有歌单")
    @GetMapping("/list")
    public R<List<MusicList>> selectMusicListByUser(Integer userId) {
        return R.succeed(musicService.addMusicListByUser(userId));
    }

    /**
     * 查找当前用户选择歌单的所有音乐
     */
    @Log(value = "查找当前用户选择歌单的所有音乐", logOperation = Log.LogOperation.IN)
    @ApiOperation(value = "查找当前用户选择歌单的所有音乐", notes = "前端播放器使用")
    @GetMapping("/list/music")
    public R<List<Music>> selectMusicByUserAndMusicList(Integer userId, Integer musicListId) {
        return R.succeed(musicService.selectMusicByUserAndMusicList(userId, musicListId));
    }

    /**
     * 添加歌单
     */
    @Log(value = "添加歌单", logOperation = Log.LogOperation.IN)
    @ApiOperation(value = "添加歌单")
    @PostMapping("/list/add")
    public R addMusicList(MusicList musicList) {
        musicService.addMusicList(musicList);
        return R.succeed();
    }

    /**
     * 更新歌单
     */
    @Log(value = "更新歌单")
    @ApiOperation(value = "更新歌单")
    @PutMapping("/list/update")
    public R updateMusicList(MusicList musicList) {
        musicService.updateMusicList(musicList);
        return R.succeed();
    }

    /**
     * 获得歌曲信息,歌单添加音乐
     *
     * @param id 网易云歌曲id 516997458
     */
    @Log(value = "网易云歌曲id获得歌曲信息,歌单添加音乐")
    @ApiOperation(value = "网易云歌曲id获得歌曲信息,歌单添加音乐")
    @PostMapping("/list/addMusic/{id}")
    public R addMusicListByUser(@PathVariable String id) {
        Music content = musicService.buildUpMusicById(id);
        content.setUserId(1);
        content.setMusicListId(1);
        musicService.addMusicListByUser(content);
        return R.succeed(content);
    }

    /**
     * 通过id获得音乐的歌词
     *
     * @param id 网易云歌曲id 516997458
     */
    @Log(value = "通过id获得音乐的歌词", logOperation = Log.LogOperation.IN)
    @ApiOperation(value = "通过id获得音乐的歌词")
    @GetMapping("/lyric/{id}")
    public R getLyricById(@PathVariable String id) {
        String lyricById = musicService.getLyricById(id);
        return R.succeed(lyricById);
    }
}

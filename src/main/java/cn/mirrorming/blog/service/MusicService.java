package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.MusicSearchParam;
import cn.mirrorming.blog.domain.dto.MusicSearchResDTO;
import cn.mirrorming.blog.domain.dto.MusicSearchResult;
import cn.mirrorming.blog.domain.dto.music.*;
import cn.mirrorming.blog.domain.po.Music;
import cn.mirrorming.blog.domain.po.MusicList;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.mapper.generate.MusicListMapper;
import cn.mirrorming.blog.mapper.generate.MusicMapper;
import cn.mirrorming.blog.service.base.AbstractBaseService;
import cn.mirrorming.blog.utils.Check;
import cn.mirrorming.blog.utils.ExceptionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import retrofit2.Call;
import retrofit2.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.mirrorming.blog.domain.enums.RespEnum.UPDATE_FAILED;

/**
 * 歌曲id:   1313354324
 * 歌曲api:  http://music.163.com/song/media/outer/url?id=1313354324.mp3
 * 搜索api:  http://music.163.com/api/search/pc {"s": "刘德华",  "offset": "0",  "limit": "10",  "type": "1"}
 * 详情api:  http://music.163.com/api/song/detail?id=xxx&ids=%5Bxxx%5D
 * 歌词api:  http://music.163.com/api/song/media?id=1313354324
 * 评论api:  http://music.163.com/api/v1/resource/comments/R_SO_4_1313354324     换页参数：http://music.163.com/api/v1/resource/comments/R_SO_4_516997458?limit=2&offset=40
 *
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/9 13:12
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MusicService
        extends AbstractBaseService {
    private static final String NETEASE_MUSIC_SEARCH_API = "http://music.163.com/api/search/pc";
    private static final String BASE_NETEASE_MUSIC_SEARCH_API = "http://music.163.com/";
    public static final String MUSIC_URL_FORMAT = "http://music.163.com/song/media/outer/url?id=%s.mp3";
    public static final String GET_MUSIC_INFO_URL_FORMAT = "http://music.163.com/api/song/detail/?id=%s&ids=\\[%s\\]";
    public static final String LYRICS_URL_FORMAT = "http://music.163.com/api/song/media?id=%s";
    public static final String COMMENT_URL_FORMAT = "http://music.163.com/api/v1/resource/comments/R_SO_4_%s";
    private static final String DEFAULT_LRC = "[00:01.000] 无歌词，请您欣赏";
    private final MusicMapper musicMapper;
    private final MusicListMapper musicListMapper;

    interface MusicApi {
        /**
         * http://music.163.com/api/search/pc
         */
        @POST("api/search/pc")
        @FormUrlEncoded
        Call<NetEaseSearchMusicDTO> netEaseMusicSearch(@FieldMap Map<String, String> map);

        /**
         * todo
         */
        @GET("api/v1/resource/comments/{id}")
        Call<NetEaseCommentDTO> getNetEaseMusicComment(@Path("id") String id,
                                                       @Query("limit") String limit,
                                                       @Query("offset") String offset);

        /**
         * http://music.163.com/api/song/detail?id= {xxx} &ids=[xxx]
         */
        @GET("api/song/detail")
        Call<MusicDetailDTO> getMusicInfo(@Query("id") String id,
                                          @Query("ids") String ids);

        /**
         * 歌词api:  http://music.163.com/api/song/media?id=1313354324
         */
        @GET("api/song/media")
        Call<LyricReturn> getMusicLrc(@Query("id") String id);
    }

    /**
     * 网易云音乐搜索
     *
     * @param params {@link MusicSearchParam}
     * @return
     * @throws Exception
     */
    public MusicSearchResDTO netEaseMusicSearch(Map<String, String> params) throws Exception {
        log.info("音乐搜索, params：{}", params);
        MusicApi musicApi = builderRetrofit(BASE_NETEASE_MUSIC_SEARCH_API).create(MusicApi.class);
        Call<NetEaseSearchMusicDTO> call = musicApi.netEaseMusicSearch(params);
        NetEaseSearchMusicDTO musicDTO = call.execute().body();
        Optional.ofNullable(musicDTO).orElseThrow(() -> new AppException("没有搜索结果"));
        List<MusicSearchResult> collect = musicDTO.getResult()
                .getSongs()
                .stream()
                .map(songs -> MusicSearchResult.builder()
                        .musicId(String.valueOf(songs.getId()))
                        .name(songs.getName())
                        .artist(songs.getArtists().stream().map(Artists::getName).reduce("", (a, b) -> a + "/" + b).substring(1))
                        .albumName(songs.getAlbum().getName())
                        .cover(songs.getAlbum().getPicUrl())
                        .url(String.format(MUSIC_URL_FORMAT, songs.getId()))
                        .lrc(getLyricById(String.valueOf(songs.getId())))
                        .build())
                .collect(Collectors.toList());
        return MusicSearchResDTO.builder().musicSearchResults(collect).songCount(musicDTO.getResult().getSongCount()).build();
    }

    /**
     * 通过id 获得音乐各项信息
     *
     * @param id
     * @throws Exception
     */
    public Music buildUpMusicById(String id) {
        log.info("通过获得音乐各项信息");
        MusicApi musicApi = builderRetrofit(BASE_NETEASE_MUSIC_SEARCH_API).create(MusicApi.class);

        //传过来网易云的音乐id，拼接成：http://music.163.com/song/media/outer/url?id=xxx.mp3
        String musicUrl = String.format(MUSIC_URL_FORMAT, id);
        //将歌曲任务与歌词任务组合一起执行，都执行完成后，将两个任务的结果合并
        CompletableFuture<Music> musicResFuture = CompletableFuture.supplyAsync(() -> {
            Call<MusicDetailDTO> call = musicApi.getMusicInfo(id, "[" + id + "]");
            try {
                MusicDetailDTO dto = call.execute().body();
                Optional.ofNullable(dto).orElseThrow(() -> new AppException("歌曲信息获取出错"));
                if (CollectionUtils.isEmpty(dto.getSongs())) {
                    log.warn("无效音乐id");
                }
                Songs song = dto.getSongs().get(0);
                return Music.builder()
                        .commentId(song.getCommentThreadId())
                        .url(musicUrl)
                        .cover(song.getAlbum().getPicUrl())
                        .artist(song.getArtists().stream().map(Artists::getName).reduce("", (a, b) -> a + "," + b).substring(1))
                        .name(song.getName())
                        .albumName(song.getAlbum().getName())
                        .musicId(id)
                        .build();
            } catch (IOException e) {
                log.warn("歌曲id:{},getMusicInfo API 出错：{}", id, e.getMessage());
                return null;
            }
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            Call<LyricReturn> call = musicApi.getMusicLrc(id);
            try {
                LyricReturn lyricReturn = call.execute().body();
                Optional.ofNullable(lyricReturn).orElseThrow(() -> new AppException("歌词获取出错"));
                return StringUtils.isEmpty(lyricReturn.getLyric()) ? DEFAULT_LRC : lyricReturn.getLyric();
            } catch (IOException e) {
                log.warn("歌曲id:{},获取歌词出错：{}", id, e.getMessage());
                return null;
            }
        }), (musicInfoRes, lyricRes) -> {
            musicInfoRes.setLrc(lyricRes);
            return musicInfoRes;
        });
        try {
            return musicResFuture.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("超时");
        }
    }


    /**
     * 通过id获得评论内容
     *
     * @param id       网易云评论id 格式类似：R_SO_4_516997458   其中516997458为歌曲id
     * @param pageSize 每页数量
     * @param pageNum  当前页
     * @return
     * @throws Exception
     */
    public NetEaseCommentDTO getNetEaseMusicComment(String id, String pageSize, String pageNum) throws IOException {
        log.info("网易云音乐通过id获得评论内容, params：id-> {},pageSize -> {}, pageNum -> {}", id, pageSize, pageNum);
        MusicApi musicApi = builderRetrofit(BASE_NETEASE_MUSIC_SEARCH_API).create(MusicApi.class);

        Call<NetEaseCommentDTO> call = musicApi.getNetEaseMusicComment(id, pageSize, pageNum);
        return call.execute().body();
    }

    /**
     * 查找当前用户所有歌单
     *
     * @return List<MusicList>
     */
    public List<MusicList> addMusicListByUser(Integer userId) {
        return musicListMapper.selectList(
                new QueryWrapper<MusicList>()
                        .orderByDesc(MusicList.COL_CREATE_TIME)
                        .and(i -> i.eq(MusicList.COL_USER_ID, userId)));
    }

    /**
     * 查找当前用户选择歌单的所有音乐
     */
    public List<Music> selectMusicByUserAndMusicList(Integer userId, Integer musicListId) {
        return musicMapper.selectList(
                new QueryWrapper<Music>()
                        .orderByAsc(Music.COL_CREATE_TIME)
                        .and(i -> i.eq(Music.COL_USER_ID, userId).eq(Music.COL_MUSIC_LIST_ID, musicListId)));
    }

    /**
     * 添加歌单
     */
    public void addMusicList(MusicList musicList) {
        int insert = musicListMapper.insert(musicList);
        Check.affectedOneRow(insert).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));
    }

    /**
     * 更新歌单
     */
    public void updateMusicList(MusicList musicList) {
        int update = musicListMapper.update(musicList, new QueryWrapper<>());
        Check.affectedOneRow(update).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));
    }

    /**
     * 歌单添加音乐
     */
    public void addMusicListByUser(Music music) {
        int insert = musicMapper.insert(music);
        Check.affectedOneRow(insert).orElseThrow(() -> ExceptionUtils.appEx(UPDATE_FAILED));
    }

    /**
     * 通过id获得音乐的歌词
     */
    public String getLyricById(String id) {
        log.info("通过id获得音乐的歌词");
        MusicApi api = builderRetrofit(BASE_NETEASE_MUSIC_SEARCH_API).create(MusicApi.class);
        try {
            LyricReturn lyricReturn = api.getMusicLrc(id).execute().body();
            Optional.ofNullable(lyricReturn).orElseThrow(() -> new AppException("歌词获取出错"));
            return StringUtils.isEmpty(lyricReturn.getLyric()) ? DEFAULT_LRC : lyricReturn.getLyric();
        } catch (IOException e) {
            log.warn("歌曲id:{},获取歌词出错：{}", id, e.getMessage());
            return null;
        }
    }

    public List<Music> getAll() {
        return musicMapper.selectList(new QueryWrapper<Music>().orderByAsc(Music.COL_ID));
    }

    public int insert(List<Music> list) {
        return musicMapper.updateBatch(list);
    }
}

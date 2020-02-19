package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.MusicSearchDto;
import cn.mirrorming.blog.domain.dto.MusicSearchResDTO;
import cn.mirrorming.blog.domain.dto.music.Artists;
import cn.mirrorming.blog.domain.dto.music.NetEaseCommentDTO;
import cn.mirrorming.blog.domain.dto.music.NetEaseSearchMusicDTO;
import cn.mirrorming.blog.domain.po.Music;
import cn.mirrorming.blog.domain.po.MusicList;
import cn.mirrorming.blog.exception.AppException;
import cn.mirrorming.blog.mapper.auto.MusicListMapper;
import cn.mirrorming.blog.mapper.auto.MusicMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.mirrorming.blog.domain.constants.SystemConstant.EFFECT_ROW;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/9 13:12
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MusicService {
    private static final String NETEASE_MUSIC_SEARCH_API = "http://music.163.com/api/search/pc";
    private static final String BASE_NETEASE_MUSIC_SEARCH_API = "http://music.163.com/";
    public static final String MUSIC_URL_FORMAT = "http://music.163.com/song/media/outer/url?id=%s.mp3";
    public static final String LYRICS_URL_FORMAT = "http://music.163.com/api/song/media?id=%s";
    public static final String COMMENT_URL_FORMAT = "http://music.163.com/api/v1/resource/comments/R_SO_4_%s";
    private static final String DEFAULT_LRC = "[00:00.000] Default Lyric \\n [00:03.000] 无歌词，请您欣赏";
    private final MusicMapper musicMapper;
    private final MusicListMapper musicListMapper;

    interface MusicApi {
        @POST("api/search/pc")
        @FormUrlEncoded
        Call<NetEaseSearchMusicDTO> netEaseMusicSearch(@FieldMap Map<String, String> map);

        @GET("api/v1/resource/comments/{id}")
        Call<NetEaseCommentDTO> getNetEaseMusicComment(@Path("id") String id,
                                                       @Query("limit") String limit,
                                                       @Query("offset") String offset);
    }

    /**
     * 构建 Retrofit
     *
     * @param baseUrl 请求 url
     * @return Retrofit
     */
    private Retrofit builderRetrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add((Interceptor.Chain chain) -> {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder()
                    .header("Content-type", "application/json; charset=utf-8")
                    .header("Accept", "application/json");
            Request build = requestBuilder.build();
            return chain.proceed(build);
        });
        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 网易云音乐搜索
     *
     * @param params {@link MusicSearchDto}
     * @return
     * @throws Exception
     */
    public List<MusicSearchResDTO> netEaseMusicSearch(Map<String, String> params) throws Exception {
        log.info("音乐搜索, params：{}", params);
        MusicApi musicApi = builderRetrofit(BASE_NETEASE_MUSIC_SEARCH_API).create(MusicApi.class);

        Call<NetEaseSearchMusicDTO> call = musicApi.netEaseMusicSearch(params);
        NetEaseSearchMusicDTO musicDTO = call.execute().body();
        Optional.ofNullable(musicDTO).orElseThrow(() -> new AppException("没有搜索结果"));
        return musicDTO.getResult()
                .getSongs()
                .stream()
                .map(songs -> MusicSearchResDTO.builder()
                        .musicId(String.valueOf(songs.getId()))
                        .name(songs.getName())
                        .artists(songs.getArtists()
                                .stream()
                                .map(Artists::getName)
                                .collect(Collectors.toList()))
                        .albumName(songs.getAlbum().getName())
                        .coverUrl(songs.getAlbum().getPicUrl())
                        .url(String.format(MUSIC_URL_FORMAT, songs.getId()))
                        .lrcUrl(String.format(LYRICS_URL_FORMAT, songs.getId()))
                        .build())
                .collect(Collectors.toList());
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
                        .orderByDesc(Music.COL_CREATE_TIME)
                        .and(i -> i.eq(Music.COL_USER_ID, userId).eq(Music.COL_MUSIC_LIST_ID, musicListId)));

    }

    /**
     * 添加歌单
     */
    public boolean addMusicList(MusicList musicList) {
        return musicListMapper.insert(musicList) == EFFECT_ROW;
    }

    /**
     * 更新歌单
     */
    public boolean updateMusicList(MusicList musicList) {
        return musicListMapper.update(musicList, new QueryWrapper<>()) == EFFECT_ROW;
    }

    /**
     * 歌单添加音乐
     */
    public boolean addMusicListByUser(Music music) {
        return musicMapper.insert(music) == EFFECT_ROW;
    }
}

package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.MusicSearchDto;
import cn.mirrorming.blog.domain.dto.music.NetEaseCommentDto;
import cn.mirrorming.blog.domain.dto.music.NetEaseSearchMusicDto;
import cn.mirrorming.blog.domain.po.Music;
import cn.mirrorming.blog.domain.po.MusicList;
import cn.mirrorming.blog.mapper.auto.MusicListMapper;
import cn.mirrorming.blog.mapper.auto.MusicMapper;
import cn.mirrorming.blog.utils.MapperUtils;
import cn.mirrorming.blog.utils.OkHttpClientUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    private final MusicMapper musicMapper;
    private final MusicListMapper musicListMapper;

    /**
     * 网易云音乐搜索
     *
     * @param params {@link MusicSearchDto}
     * @return
     * @throws Exception
     */
    public NetEaseSearchMusicDto netEaseMusicSearch(Map params) throws Exception {
        Response response = OkHttpClientUtil.getInstance().postData(NETEASE_MUSIC_SEARCH_API, params);
        return MapperUtils.json2pojo(response.toString(), NetEaseSearchMusicDto.class);
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
    public NetEaseCommentDto getNetEaseMusicComment(String id, String pageSize, String pageNum) {
        Response data = OkHttpClientUtil.getInstance().getData("http://music.163.com/api/v1/resource/comments/" + id + "?limit=" + pageSize + "&offset=" + pageNum);
        NetEaseCommentDto dto = null;
        try {
            dto = MapperUtils.json2pojo(data.toString(), NetEaseCommentDto.class);
        } catch (Exception e) {
            log.warn("当前链接:{},获取歌曲评论出错：{}", "http://music.163.com/api/v1/resource/comments/" + id + "?limit=" + pageSize + "&offset=" + pageNum, e.getMessage());
        }
        return dto;
    }

    /**
     * 查找当前用户所有歌单
     *
     * @return
     */
    public List<MusicList> selectMusicListByUser(Integer userId) {
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
     * 添加歌单
     */
    public boolean updateMusicList(MusicList musicList) {
        return musicListMapper.update(
                musicList,
                new QueryWrapper<>()) == EFFECT_ROW;
    }

    /**
     * 歌单添加音乐
     */
    public boolean selectMusicListByUser(Music music) {
        return musicMapper.insert(music) == EFFECT_ROW;
    }
}

package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.dto.bilibili.BilibiliFans;
import cn.mirrorming.blog.domain.dto.bilibili.BilibiliVideo;
import cn.mirrorming.blog.service.base.AbstractBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/26 14:15
 */

@Slf4j
@Service
public class BilibiliService
        extends AbstractBaseService {

    private static final String BASE_GET_FANS_NUMBER = "https://api.bilibili.com/";
    private static final String BASE_GET_HISTORY_VIDEO = "http://space.bilibili.com/";
    private static final String GET_FANS_NUMBER_FORNAT = "https://api.bilibili.com/x/relation/stat?vmid=%s&jsonp=%s";
    private static final String GET_HISTORY_VIDEO_FORMAT = "http://space.bilibili.com/ajax/member/getSubmitVideos?mid=%s&page=%s&pagesize=%s&tid=0&keyword=&order=pubdate";

    interface BilibiliRequest {
        @GET("x/relation/stat")
        Call<BilibiliFans> getGetFansNumberByBiliUserId(@Query("vmid") String vmid,
                                                        @Query("jsonp") String jsonp);

        @GET("ajax/member/getSubmitVideos")
        Call<BilibiliVideo> getGetHistoryVideoByBiliUserId(@Query("biliUserId") int biliUserId,
                                                           @Query("pageNum") int pageNum,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("keyword") String keyword,
                                                           @Query("order") String order);
    }

    /**
     * 通过b站的用户 id 获得该用户的粉丝数
     *
     * @param biliUserId 用户 id
     * @return {@link BilibiliFans}
     * @throws Exception
     */
    public BilibiliFans getGetFansNumberByBiliUserId(String biliUserId) throws Exception {
        log.info("查询Bilibili粉丝数量, 用户id：{}", biliUserId);
        Retrofit retrofit = builderRetrofit(BASE_GET_FANS_NUMBER);

        BilibiliRequest bilibili = retrofit.create(BilibiliRequest.class);
        Call<BilibiliFans> call = bilibili.getGetFansNumberByBiliUserId(biliUserId, "jsonp");
        return call.execute().body();
    }

    /**
     * 通过b站的用户 id 获得该用户的投稿视频
     *
     * @param biliUserId b站用户id
     * @param pageNum    第几页
     * @param pageSize   每页大小
     * @param keyword    按照关键字过滤
     * @param order      排序规则
     * @return
     */
    public BilibiliVideo getGetHistoryVideoByBiliUserId(int biliUserId, int pageNum, int pageSize, String keyword, String order) throws Exception {
        log.info("通过b站的用户 id 获得该用户的投稿视频, id {}", biliUserId);
        Retrofit retrofit = builderRetrofit(BASE_GET_HISTORY_VIDEO);
        BilibiliRequest bilibili = retrofit.create(BilibiliRequest.class);
        Call<BilibiliVideo> call = bilibili.getGetHistoryVideoByBiliUserId(biliUserId, pageNum, pageSize, keyword, order);
        return call.execute().body();
    }
}

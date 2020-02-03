package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.dto.base.ResultData;
import cn.mirrorming.blog.domain.dto.bilibili.BilibiliFans;
import cn.mirrorming.blog.domain.dto.bilibili.BilibiliVideo;
import cn.mirrorming.blog.service.BilibiliService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/27 12:08
 */
@RestController
@RequestMapping("bilibili")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BilibiliController {
    private final BilibiliService bilibiliService;

    /**
     * 通过b站的用户 id 获得该用户的粉丝数
     */
    @GetMapping("fans/{id}")
    public ResultData archives(@PathVariable("id") String id) throws Exception {
        BilibiliFans bilibiliFans = bilibiliService.getGetFansNumberByBiliUserId(id);
        return ResultData.succeed(bilibiliFans);
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
    @GetMapping("video")
    public ResultData getGetHistoryVideoByBiliUserId(int biliUserId, int pageNum, int pageSize, String keyword, String order) throws Exception {
        BilibiliVideo bilibiliVideo = bilibiliService.getGetHistoryVideoByBiliUserId(biliUserId, pageNum, pageSize, keyword, order);
        return ResultData.succeed(bilibiliVideo);
    }
}

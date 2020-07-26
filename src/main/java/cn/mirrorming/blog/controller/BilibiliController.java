package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.aop.logger.Log;
import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.dto.bilibili.BilibiliFans;
import cn.mirrorming.blog.domain.dto.bilibili.BilibiliVideo;
import cn.mirrorming.blog.service.BilibiliService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/27 12:08
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "bilibili")
@RestController
@RequestMapping("bilibili")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BilibiliController {
    private final BilibiliService bilibiliService;

    @Log("通过b站的用户 id 获得该用户的粉丝数")
    @ApiOperation(value = "通过b站的用户 id 获得该用户的粉丝数")
    @GetMapping("fans/{id}")
    public R archives(@PathVariable("id") String id) throws Exception {
        BilibiliFans bilibiliFans = bilibiliService.getGetFansNumberByBiliUserId(id);
        return R.succeed(bilibiliFans);
    }

    @Log("通过b站的用户 id 获得该用户的投稿视频")
    @ApiOperation(value = "通过b站的用户 id 获得该用户的投稿视频")
    @GetMapping("video")
    public R getGetHistoryVideoByBiliUserId(
            @ApiParam(value = "b站用户id") @RequestParam(value = "biliUserId") int biliUserId,
            @ApiParam(value = "第几页") @RequestParam(value = "pageNum") int pageNum,
            @ApiParam(value = "每页大小") @RequestParam(value = "pageSize") int pageSize,
            @ApiParam(value = "按照关键字过滤") @RequestParam(value = "keyword") String keyword,
            @ApiParam(value = "排序规则") @RequestParam(value = "order") String order) throws Exception {
        BilibiliVideo bilibiliVideo = bilibiliService.getGetHistoryVideoByBiliUserId(biliUserId, pageNum, pageSize, keyword, order);
        return R.succeed(bilibiliVideo);
    }
}

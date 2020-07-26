package cn.mirrorming.blog.task;

import cn.mirrorming.blog.service.BilibiliService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/12/27 12:20
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BilibiliTask {
    private final BilibiliService bilibiliService;

    /**
     * 每天0点 更新粉丝数量 查看每天的粉丝增长数
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void updateFansGrowNumber() {
//todo 查看每天的粉丝增长数
    }
}

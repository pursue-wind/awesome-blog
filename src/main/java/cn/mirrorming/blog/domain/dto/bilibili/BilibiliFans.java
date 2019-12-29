package cn.mirrorming.blog.domain.dto.bilibili;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/26 14:23
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BilibiliFans {

    private int code;
    private String message;
    private int ttl;
    private FansData data;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class FansData {
        private int mid;
        private int following;
        private int whisper;
        private int black;
        private int follower;
    }
}

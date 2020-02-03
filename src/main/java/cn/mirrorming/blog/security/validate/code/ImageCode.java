package cn.mirrorming.blog.security.validate.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/2 22:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageCode {
    private BufferedImage image;
    private String code;
    private LocalDateTime expireTime;

    /**
     * @param image    图片
     * @param code     验证码
     * @param expireIn 多少秒内过期
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
}

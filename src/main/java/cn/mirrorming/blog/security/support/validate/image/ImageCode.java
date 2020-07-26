package cn.mirrorming.blog.security.support.validate.image;

import cn.mirrorming.blog.security.support.validate.ValidateCode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.image.BufferedImage;

/**
 * 图片验证码
 *
 * @author Mireal
 */
public class ImageCode extends ValidateCode {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private transient BufferedImage image;

    public static ImageCode of(BufferedImage image, String code, int expireIn) {
        return new ImageCode(image, code, expireIn);
    }

    private ImageCode(BufferedImage image, String code, Integer expireIn) {
        super(code, expireIn);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}

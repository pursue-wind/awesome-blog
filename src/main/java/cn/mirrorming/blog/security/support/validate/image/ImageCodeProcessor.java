package cn.mirrorming.blog.security.support.validate.image;

import cn.mirrorming.blog.security.support.validate.ValidateCode;
import cn.mirrorming.blog.security.support.validate.ValidateCodeGenerator;
import cn.mirrorming.blog.security.support.validate.ValidateCodeType;
import cn.mirrorming.blog.security.support.validate.impl.AbstractValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * 图片验证码处理器
 *
 * @author Mireal
 */
@Component
public class ImageCodeProcessor extends AbstractValidateCodeProcessor {

    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    /**
     * 发送图形验证码，将其写到响应中
     */
    @Override
    public void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        ImageCode imageCode;
        if (validateCode instanceof ImageCode) {
            imageCode = (ImageCode) validateCode;
        } else {
            throw new IllegalArgumentException("需要传入参数类型:" + ImageCode.class);
        }

        ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }

    @Override
    protected ValidateCodeType getValidateCodeType() {
        return ValidateCodeType.image;
    }

    @Override
    protected ValidateCode generate(ServletWebRequest request) {
        return imageCodeGenerator.generate(request);
    }

}

package cn.mirrorming.blog.domain.dto.article;

import cn.mirrorming.blog.domain.po.Article;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2020/2/26 20:13
 */
@Data
@Builder
public class ArchivesDTO {
    private User user;
    private Article article;
}

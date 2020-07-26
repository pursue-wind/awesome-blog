package cn.mirrorming.blog.domain.dto;

import lombok.Data;

/**
 * @author Mireal Chan
 */
@Data
public class TokenDTO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
}
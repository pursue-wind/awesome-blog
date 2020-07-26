package cn.mirrorming.blog.security;

import cn.mirrorming.blog.domain.po.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 用户权限认证信息
 *
 * @author Mireal
 * @date 2020/7/15 17:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MyUserDetails implements UserDetails, SocialUserDetails, Serializable {

    private Integer id;
    /**
     * 账号
     */
    private String username;
    private String email;
    private String avatar;
    @JsonIgnore
    private String password;
    /**
     * 是否冻结，1为冻结，0为不冻结
     */
    private Boolean isFreeze;

    @JsonIgnore
    private List<GrantedAuthority> authorityList;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(isFreeze, false);
    }

    @Override
    public String getUserId() {
        return String.valueOf(this.id);
    }

}

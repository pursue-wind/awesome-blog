package cn.mirrorming.blog.security;


import cn.mirrorming.blog.mapper.generate.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Mireal
 */
@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        Object principal = authentication.getPrincipal();
        MyUserDetails authInfo;
        if (principal instanceof MyUserDetails) {
            authInfo = (MyUserDetails) principal;
        } else {
            return false;
        }
        switch (String.valueOf(targetDomainObject)) {
            case "project": {
                String projectId = String.valueOf(permission);
                return projectId.equals(233 + "");
            }
            default:
                return false;
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}

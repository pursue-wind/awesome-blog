package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.po.Permission;
import cn.mirrorming.blog.mapper.PermissionQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mireal Chan
 * @version V1.0
 * @date 2019/11/8 22:09
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionService {
    private final PermissionQueryMapper permissionQueryMapper;

    public List<Permission> selectPermissionByUserId(Integer id) {
        return permissionQueryMapper.selectPermissionByUserId(id);
    }
}

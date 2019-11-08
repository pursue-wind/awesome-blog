package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.po.SecurityPermission;
import cn.mirrorming.blog.mapper.PermissionQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/11/8 22:09
 */
@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionQueryMapper permissionQueryMapper;

    public List<SecurityPermission> selectPermissionByUserId(Integer id) {
        return permissionQueryMapper.selectPermissionByUserId(id);
    }
}

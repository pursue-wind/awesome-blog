package cn.mirrorming.blog.security.service.impl;

import cn.mirrorming.blog.mapper.PermissionQueryMapper;
import cn.mirrorming.blog.mapper.auto.PermissionMapper;
import cn.mirrorming.blog.domain.po.SecurityPermission;
import cn.mirrorming.blog.security.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, SecurityPermission> implements PermissionService {
    @Resource
    private PermissionQueryMapper permissionQueryMapper;

    @Override
    public List<SecurityPermission> selectPermissionByUserId(Integer id) {
        return permissionQueryMapper.selectPermissionByUserId(id);
    }
}


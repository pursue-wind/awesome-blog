package cn.mirrorming.blog.security.service;

import cn.mirrorming.blog.domain.po.SecurityPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PermissionService extends IService<SecurityPermission> {

    List<SecurityPermission> selectPermissionByUserId(Integer id);
}


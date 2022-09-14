package com.minzheng.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minzheng.blog.dao.FriendLinkDao;
import com.minzheng.blog.dto.FriendLinkBackDTO;
import com.minzheng.blog.dto.FriendLinkDTO;
import com.minzheng.blog.entity.FriendLink;
import com.minzheng.blog.util.BeanCopyUtils;
import com.minzheng.blog.util.PageUtils;
import com.minzheng.blog.vo.ConditionVO;
import com.minzheng.blog.vo.FriendLinkVO;
import com.minzheng.blog.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 友情链接服务
 *
 * @author xiaojie
 */
@Service
public class FriendLinkService extends ServiceImpl<FriendLinkDao, FriendLink>  {
    @Autowired
    private FriendLinkDao friendLinkDao;


    public List<FriendLinkDTO> listFriendLinks() {
        // 查询友链列表
        List<FriendLink> friendLinkList = friendLinkDao.selectList(null);
        return BeanCopyUtils.copyList(friendLinkList, FriendLinkDTO.class);
    }


    public PageResult<FriendLinkBackDTO> listFriendLinkDTO(ConditionVO condition) {
        // 分页查询友链列表
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<FriendLink> friendLinkPage = friendLinkDao.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), FriendLink::getLinkName, condition.getKeywords()));
        // 转换DTO
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtils.copyList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageResult<>(friendLinkBackDTOList, friendLinkPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)

    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        FriendLink friendLink = BeanCopyUtils.copyObject(friendLinkVO, FriendLink.class);
        this.saveOrUpdate(friendLink);
    }

}

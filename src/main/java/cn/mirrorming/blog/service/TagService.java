package cn.mirrorming.blog.service;

import cn.mirrorming.blog.domain.constants.SystemConstant;
import cn.mirrorming.blog.domain.po.Tag;
import cn.mirrorming.blog.mapper.auto.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagService {
    private final TagMapper tagMapper;

    public List<Tag> selectAllTag() {
        return tagMapper.selectList(null);
    }

    public boolean addTag(Tag tag) {
        return tagMapper.insert(tag) == SystemConstant.EFFECT_ROW;
    }

    public boolean updateTag(Tag tag) {
        return tagMapper.updateById(tag) == SystemConstant.EFFECT_ROW;
    }
}

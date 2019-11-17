package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.dto.base.ResultData;
import cn.mirrorming.blog.domain.po.Tag;
import cn.mirrorming.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author mirror
 * @Date 2019/9/6 11:32
 * @since v1.0.0
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("tag")
public class TagController {
    private final TagService tagService;

    @GetMapping("all")
    public ResultData selectAllArticle() {
        List<Tag> tags = tagService.selectAllTag();
        return ResultData.succeed(tags);
    }

    /**
     * 添加标签
     */
    @PostMapping("add")
    public ResultData addTag(Tag tag) {
        return tagService.addTag(tag) ?
                ResultData.succeed() : ResultData.fail("添加失败");
    }

    /**
     * 更新标签
     */
    @PutMapping("update")
    public ResultData deleteCategory(Tag tag) {
        return tagService.updateTag(tag) ?
                ResultData.succeed() : ResultData.fail("删除失败");
    }
}

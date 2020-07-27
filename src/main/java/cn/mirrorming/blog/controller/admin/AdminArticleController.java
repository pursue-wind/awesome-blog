package cn.mirrorming.blog.controller.admin;

import cn.mirrorming.blog.aop.cache.Cache;
import cn.mirrorming.blog.aop.logger.Log;
import cn.mirrorming.blog.controller.base.BaseController;
import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.dto.article.AddArticleDTO;
import cn.mirrorming.blog.domain.po.ArticleContent;
import cn.mirrorming.blog.security.SecurityUtil;
import cn.mirrorming.blog.service.admin.AdminArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mireal Chan
 * @since v1.0.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "文章管理")
@RestController
@RequestMapping("admin/article")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminArticleController extends BaseController {
    private final AdminArticleService articleService;

    /**
     * 文章列表
     */
    @Log("文章列表")
    @ApiOperation(value = "文章列表")
    @GetMapping("list/{categoryId}")
    public R selectAllArticle(@PathVariable(required = false) Integer categoryId) {
        return R.succeed(articleService.selectAllArticle(SecurityUtil.getUserId(), categoryId));
    }

    @Log("根据文章id获得文章的内容")
    @ApiOperation(value = "根据文章id获得文章的内容")
    @GetMapping("content/{id}")
    public R selectArticleById(@PathVariable Integer id) {
        ArticleContent articleContent = articleService.selectContentArticleById(id);
        return R.succeed(articleContent);
    }

    @Log("添加文章")
    @ApiOperation(value = "添加文章")
    @PostMapping("add")
    public R newArticle(@RequestBody AddArticleDTO data) {
        data.setUserId(SecurityUtil.getUserId());
        articleService.newArticle(data);
        return R.succeed();
    }

    @Log("编辑文章")
    @ApiOperation(value = "编辑文章")
    @PostMapping("edit/{id}")
    @Cache("delete_by_prefix => 'article::ArticleList:','article::ArticleContent:' + #id + ':'")
    public R editArticle(@PathVariable("id") Integer id, @RequestBody AddArticleDTO data) {
        data.setUserId(SecurityUtil.getUserId());
        articleService.editArticle(id, data);

        // publish文章列表变动事件
        publishArticleEditEvent();
        return R.succeed();
    }

    @Log("删除文章")
    @ApiOperation(value = "删除文章")
    @DeleteMapping("delete/{id}")
    public R newArticle(@PathVariable("id") Integer articleId) {
        articleService.deleteArticle(articleId, SecurityUtil.getUserId());

        // publish文章列表变动事件
        publishArticleEditEvent();
        return R.succeed();
    }
}
package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.aop.logger.Log;
import cn.mirrorming.blog.controller.base.BaseController;
import cn.mirrorming.blog.domain.dto.article.ArticleDTO;
import cn.mirrorming.blog.domain.dto.article.ArticleListDTO;
import cn.mirrorming.blog.domain.dto.base.PageDTO;
import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mireal Chan
 * @since v1.0.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(tags = "文章")
@RestController
@RequestMapping("article")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleController extends BaseController {
    private final ArticleService articleService;
    private final ApplicationContext applicationContext;

    @Log("文章列表")
    @ApiOperation(value = "文章列表")
    @GetMapping("list/{cur}")
    public PageDTO<List<ArticleListDTO>> selectAllArticle(@PathVariable Integer cur, @RequestParam(required = false, defaultValue = "0") Integer categoryId) {
        return articleService.selectArticlePage(cur, 10, categoryId);
    }

    @Log("归档页面文章按时间显示")
    @ApiOperation(value = "归档页面文章按时间显示")
    @GetMapping("archives")
    public R archives() {
        return R.succeed(articleService.filedArticle());
    }

    @Log(value = "点击文章获得文章所有信息", logOperation = Log.LogOperation.IN)
    @ApiOperation(value = "点击文章获得文章所有信息")
    @GetMapping("{id}")
    public R selectArticleById(@ApiParam(value = "文章id") @PathVariable Integer id,
                               @ApiParam(value = "文章阅读密码") @RequestParam(value = "readPassword", required = false) String readPassword) {
        ArticleDTO articleDto = articleService.selectArticleById(id, null == readPassword ? "" : readPassword);
        //文章点击事件
        publishArticleClickEvent(id);
        return R.succeed(articleDto);
    }
}
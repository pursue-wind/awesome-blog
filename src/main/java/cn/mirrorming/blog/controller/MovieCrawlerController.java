package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.utils.spider.MovieListParser;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.ReentrantLock;

/**
 @author mireal
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
public class MovieCrawlerController {
    private static final ReentrantLock LOCK = new ReentrantLock();
    @Autowired
    private MovieListParser movieListParser;

    @ApiOperation(value = "爬虫")
    @GetMapping("/crawl")
    public String crawl(@RequestParam(value = "p", required = false, defaultValue = MovieListParser.START_PAGE) String page) {
        try {
            if (LOCK.tryLock()) {
                movieListParser.parse(MovieListParser.START_PAGE);
                return "爬虫执行完成";
            } else {
                return "请勿重复执行";
            }
        } catch (Exception e) {
            log.error("爬虫发生异常", e);
            return e.getMessage();
        } finally {
            if (LOCK.isHeldByCurrentThread()) {
                LOCK.unlock();
            }
        }
    }
}

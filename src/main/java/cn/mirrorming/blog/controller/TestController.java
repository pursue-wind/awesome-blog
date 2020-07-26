package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.aop.cache.Cache;
import cn.mirrorming.blog.utils.RedisOperator;
import cn.mirrorming.blog.service.ArticleService;
import cn.mirrorming.blog.utils.TestUtil;
import cn.mirrorming.blog.utils.spider.XiCiIpProxyParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Mireal Chan
 * @Date 2019/9/6 15:00
 * @since v1.0.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
public class TestController {
    @Autowired
    private XiCiIpProxyParser xiCiIpProxyParser;
    @Autowired
    ArticleService articleService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedisOperator redisOperator;

    @Cache("delete => #id,'pre'+ #name + 'suffix','customer'")
    @GetMapping("/test/cacheannotation")
    public String testMyAno(Integer id, String name) throws Exception {

        return "/test testMyAno over";
    }

    @GetMapping("/test/cache/insert")
    public String testAddCache(Integer id, String name) throws Exception {
        redisOperator.set("123", "123");
        redisOperator.set("qwe", "qwe");
        return "/test testMyAno over";
    }

    @GetMapping("/testxi")
    public String xici() throws Exception {
        IntStream.range(1, 3000).parallel().forEach(i -> {
            log.info("正在测试第{}页", i);
            try {
                xiCiIpProxyParser.parse(String.valueOf(i));
            } catch (IOException e) {
                log.warn("xici error");
            }
        });
        return "/test xici over";
    }

    @GetMapping("/testredis")
    public String red() throws Exception {
        TestUtil.testGetClassName();
        redisTemplate.boundValueOps("123").set("test", 10, TimeUnit.SECONDS);
        redisTemplate.boundValueOps("测试").set("opt", 11, TimeUnit.SECONDS);
        redisTemplate.boundValueOps("测试2").set("opt2", 12, TimeUnit.SECONDS);
        redisTemplate.boundValueOps("test2").set("opt3", 13, TimeUnit.SECONDS);
        redisOperator.set("123", "测试", 10);
        redisOperator.set("1234", "测试4", 5);
        return "/test xici over";
    }

    @GetMapping("/testxi2")
    public String xici2(String ip, int port) throws Exception {

        boolean b = xiCiIpProxyParser.testIp(ip, port);
        System.out.println(b);
        return "/test xici over";
    }

    @GetMapping("/")
    public String retNothing() {
        return "/";
    }

    @GetMapping("/view/ok")
    public String testview() {
        return "article_view";
    }

    @GetMapping("/insert/ok")
    public String article_insert() {
        return "article_insert";
    }

    @GetMapping("/update/ok")
    public String article_update() {
        return "article_update";
    }

    @PreAuthorize("hasAuthority('SystemUserDelete')")
    @GetMapping("/delete/ok")
    public String article_delete() {
        return "article_delete";
    }


}

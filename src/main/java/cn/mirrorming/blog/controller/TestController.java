package cn.mirrorming.blog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mireal
 * @Date 2019/9/6 15:00
 * @since v1.0.0
 */
@RestController
public class TestController {
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

package cn.mirrorming.blog.controller;

import cn.mirrorming.blog.domain.common.R;
import cn.mirrorming.blog.domain.es.EsMovie;
import cn.mirrorming.blog.domain.po.Movie;
import cn.mirrorming.blog.repository.MovieRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mireal Chan
 **/
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/es/movie")
public class EsMovieController {
    @Autowired
    private MovieRepository repository;

    @GetMapping("/all")
    public R getAllMovie() {
        try {
            Pageable pageable = PageRequest.of(1, 10);
            Page<EsMovie> all = repository.findAll(pageable);
            return R.succeed(all);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/remove/{id}")
    public R removeMovieById(@PathVariable Integer id) {
        repository.deleteById(id);
        return R.succeed();
    }

    @GetMapping("/remove/all")
    public R removeAllMovie() {
        repository.deleteAll();
        return null;
    }

    @GetMapping("/page/{curPage}")
    public R getMoviesPage(@PathVariable Integer curPage) {
        Page<EsMovie> result = repository.findAll(PageRequest.of(curPage, 10));
        return R.succeed(result);
    }

    @GetMapping("/searchByActor")
    public R findByActor(String condition) {
        Page<EsMovie> result = repository.findByActor(condition, PageRequest.of(1, 10));
        return R.succeed(result);
    }

    @GetMapping("/clean")
    public R clean() {
        Iterable<EsMovie> all = repository.findAll();
        all.forEach(a -> {
            if (null == a.getName() || StringUtils.isBlank(a.getScore()) || a.getName().contains("�")) {
                System.out.println(a);
                repository.deleteById(a.getId());
            }

        });
        return R.succeed("ok");
    }

    @GetMapping("/search")
    public R findByCondition(@RequestParam(value = "condition", required = true) String condition,
                             @RequestParam(value = "pageNum", defaultValue = "0", required = true) Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10", required = true) Integer pageSize) {
        QueryBuilder qb = QueryBuilders.multiMatchQuery(condition, "name", "title", "director", "actor", "category", "description", "origin");
        Page<EsMovie> result = repository.search(qb, PageRequest.of(pageNum, pageSize));
        return R.succeed(result);
    }

    @GetMapping("/findtest")
    public R findtest(String condition) {
        //        QueryBuilder qb = QueryBuilders.queryStringQuery("+" + condition);
        //        QueryBuilder qb = QueryBuilders.boolQuery().should(QueryBuilders.multiMatchQuery(condition, "name", "title", "director", "actor", "category", "description", "origin"));

        //范围查询
        QueryBuilder qb1 = QueryBuilders.rangeQuery("updateDate").from("2018-03-03").to("2019-03-03").format("yyyy-MM-dd");
        //前缀查询
        QueryBuilder qb2 = QueryBuilders.prefixQuery("title", "陈");
        //通配符查询
        QueryBuilder qb3 = QueryBuilders.wildcardQuery("title", "陈*");
        //模糊查询
        QueryBuilder qb4 = QueryBuilders.fuzzyQuery("title", "刘华");
        return R.succeed();
    }
}
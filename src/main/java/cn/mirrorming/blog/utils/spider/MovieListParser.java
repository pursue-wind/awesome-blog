package cn.mirrorming.blog.utils.spider;

import cn.mirrorming.blog.domain.es.EsMovie;
import cn.mirrorming.blog.domain.po.Movie;
import cn.mirrorming.blog.mapper.generate.MovieMapper;
import cn.mirrorming.blog.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mireal Chan
 */
@Component
@Slf4j
public class MovieListParser {
    public static final String START_PAGE = "https://www.dy2018.com/html/gndy/dyzz/index_80.html";
    @Autowired
    private MovieDetailParser movieDetailParser;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MovieMapper movieMapper;

    private Set<String> ids = new HashSet<>();


    public void parse(String url) throws Exception {
        log.info("抓取列表页面:{}", url);
        Connection.Response response = null;
        while (response == null) {
            Thread.sleep(1000);
            response = JsoupUtil.getResponse(url);
            if (response.statusCode() != 200) {
                response = null;
            }
        }

        Document document = response.parse();
        document.select(".co_content8 a").forEach(a -> {
            String href = a.attr("href");
            if (href.matches("/i/[0-9]+.html")) {
                String id = href.substring(3, href.lastIndexOf("."));
                if (ids.contains(id)) {
                    log.info("已经抓取过该电影，不再重复抓取");
                } else {
                    log.info("开始抓取电影id:{}", id);
                    try {
                        Thread.sleep(500);
                        EsMovie esMovie = movieDetailParser.parse(id);
                        Thread.sleep(500);
                        if (esMovie != null && esMovie.getActor().size() != 0) {
                            movieRepository.save(esMovie);
                            movieMapper.insert(new Movie(esMovie));
                        }
                        System.out.println(esMovie);
                        ids.add(id);
                    } catch (Exception e) {
                        log.error("抓取电影id:{}异常", id, e);
                    }
                }
            }
        });
        document.select("div.x a").forEach(a -> {
            String text = a.text();
            if ("下一页".equals(text)) {
                try {
                    //recursion
                    parse(a.absUrl("href"));
                } catch (Exception e) {
                    log.error("抓取下一页异常:{}", e);
                }
            }
        });
    }
}

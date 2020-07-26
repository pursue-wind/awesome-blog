package cn.mirrorming.blog.utils.spider;

import cn.mirrorming.blog.domain.es.EsMovie;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Mireal Chan
 */
@Component
@Slf4j
public class MovieDetailParser {
    public static final String URL_PATTERN = "https://www.dy2018.com/i/{0}.html";

    public EsMovie parse(String id) throws IOException {
        try {
            String url = MessageFormat.format(URL_PATTERN, id);
            Connection.Response response = null;
            while (response == null) {
                Thread.sleep(1000);
                response = JsoupUtil.getResponse(url);
                if (response.statusCode() != 200) {
                    response = null;
                }
            }
            Document document = response.parse();
            EsMovie esMovie = new EsMovie();
            esMovie.setId(Integer.valueOf(id));
//#Zoom > img:nth-child(42) #Zoom > img:nth-child(34)
            String title = document.select(".title_all h1").text();
            log.info("标题:{}", title);
            esMovie.setTitle(title);

            String score = document.select("strong.rank").text();
            log.info("评分:{}", score);
            esMovie.setScore(score);

            Elements zoom = document.select("#Zoom");
            String coverUrl = zoom.select("img").get(0).attr("src");
            String screenShot = zoom.select("img").get(1).attr("src");
            log.info("封面图片:{}", coverUrl);
            log.info("影视截图:{}", screenShot);
            esMovie.setCoverUrl(coverUrl);
            esMovie.setScreenshot(screenShot);
            Arrays.stream(zoom.text().split("◎"))
                    .filter(StringUtils::isNotBlank)
                    .forEach(t -> {
                        if (t.startsWith("译　　名")) {
                            log.info("译名:{}", t);
                            esMovie.setTranslatedName(Arrays.asList(t.substring(5).trim().split("/")));
                        } else if (t.startsWith("片　　名")) {
                            log.info("片名:{}", t);
                            esMovie.setName(t.substring(5).trim());
                        } else if (t.startsWith("年　　代")) {
                            log.info("年代:{}", t);
                            esMovie.setYear(t.substring(5).trim());
                        } else if (t.startsWith("产　　地")) {
                            log.info("产地:{}", t);
                            esMovie.setOrigin(t.substring(5).trim());
                        } else if (t.startsWith("类　　别")) {
                            log.info("类别:{}", t);
                            esMovie.setCategory(Arrays.asList(t.substring(5).trim().split("/")));
                        } else if (t.startsWith("片　　长")) {
                            log.info("片长:{}", t);
                            esMovie.setDuration(t.substring(5).trim());
                        } else if (t.startsWith("导　　演")) {
                            log.info("导演:{}", t);
                            esMovie.setDirector(Arrays.stream(t.substring(5).split(String.valueOf((char) 12288)))
                                    .filter(StringUtils::isNotBlank)
                                    .map(String::trim)
                                    .reduce("", (a, b) -> a + "/" + b)
                                    .substring(1));
                        } else if (t.startsWith("上映日期")) {
                            log.info("上映日期:{}", t);
                            esMovie.setReleaseDate(t.substring(5).trim());
                        } else if (t.startsWith("主　　演") || t.startsWith("　　　　　")) {
                            log.info("主演:{}", t);
                            esMovie.setActor(Arrays.stream(t.substring(5).split(String.valueOf((char) 12288)))
                                    .filter(StringUtils::isNotBlank)
                                    .map(String::trim)
                                    .collect(Collectors.toList()));
                        } else if (t.startsWith("简　　介")) {
                            log.info("简介:{}", t);
                            esMovie.setDescription(t.substring(5).replace((char) 12288, ' ').trim());
                        }
                    });
            esMovie.setDownloadUrl(zoom.select("table a").stream()
                    .map(link -> link.attr("href").trim())
                    .collect(Collectors.toList()));
            return esMovie;
        } catch (Exception e) {
            log.info("爬电影出错：{}", e.getMessage());
            return null;
        }
    }


    public static void main(String[] args) {
        String s = " 涨　　三　　点 d我的 钱 ";
        String[] split = s.split(String.valueOf((char) 12288));
        Arrays.asList(split).forEach(System.out::println);
    }
}

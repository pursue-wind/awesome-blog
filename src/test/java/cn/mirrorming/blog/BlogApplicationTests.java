package cn.mirrorming.blog;

import cn.mirrorming.blog.domain.dto.article.ArticleListDto;
import cn.mirrorming.blog.domain.po.Article;
import cn.mirrorming.blog.service.ArticleService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    ArticleService articleService;


    @Test
    void filedArticle() {
        HashMap<Integer, Map<Integer, List<Article>>> map = articleService.filedArticle();
        System.out.println(map);
    }

    @Test
    void selectAllArticle() {
        List<ArticleListDto> articleListDtos = articleService.selectAllArticle(1, 10);
        System.out.println(articleListDtos);
    }

    @Test
    void retrofitTest() throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add((Interceptor.Chain chain) -> {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder()
                    .header("Content-type", "application/json; charset=utf-8")
                    .header("Accept", "application/json");
            Request build = requestBuilder.build();
            return chain.proceed(build);
        });
        OkHttpClient client = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.bilibili.com/")
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}

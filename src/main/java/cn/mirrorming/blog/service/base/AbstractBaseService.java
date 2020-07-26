package cn.mirrorming.blog.service.base;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Mireal
 * @version V1.0
 * @date 2020/7/1 2:08
 */
public abstract class AbstractBaseService {
    /**
     * 构建 Retrofit
     *
     * @param baseUrl 请求 url
     * @return Retrofit
     */
    protected Retrofit builderRetrofit(String baseUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add((Interceptor.Chain chain) -> {
            Request request = chain.request();
            Request.Builder requestBuilder = request.newBuilder()
                    .header("Content-type", "application/json; charset=utf-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                    .header("Accept", "application/json");
            Request build = requestBuilder.build();
            return chain.proceed(build);
        });
        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

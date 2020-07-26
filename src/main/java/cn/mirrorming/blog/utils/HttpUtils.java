package cn.mirrorming.blog.utils;

import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    private static PoolingHttpClientConnectionManager cm;
    private static String UTF_8 = "UTF-8";
    private static SSLConnectionSocketFactory sslsf = null;
    private static SSLContextBuilder builder = null;
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private static void init() {
        if (cm == null) {
            cm = new PoolingHttpClientConnectionManager();
            // 整个连接池最大连接数
            cm.setMaxTotal(50);
            // 每路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(5);
        }
    }

    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true);
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            //max connection
            cm.setMaxTotal(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过连接池获取HttpClient
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
    }


    public static HttpResult get(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        return getResult(httpGet);
    }

    public static HttpResult get(String url, Map<String, String> params) throws Exception {
        URIBuilder ub = new URIBuilder(url);
        ub.setParameters(covertParams(params));
        HttpGet httpGet = new HttpGet(ub.build());
        return getResult(httpGet);
    }

    public static HttpResult get(String url, Map<String, String> headers, Map<String, String> params)
            throws Exception {
        URIBuilder ub = new URIBuilder(url);

        ArrayList<NameValuePair> pairs = covertParams(params);
        ub.setParameters(pairs);

        HttpGet httpGet = new HttpGet(ub.build());
        for (Map.Entry<String, String> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), param.getValue());
        }
        return getResult(httpGet);
    }

    public static HttpResult post(String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }

    public static HttpResult post(String url, String body) throws Exception {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");
        return getResult(httpPost);
    }


    public static HttpResult post(String url, Map<String, Object> headers, String body) throws Exception {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        httpPost.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Accept", "application/json");

        return getResult(httpPost);
    }

    /**
     * Put
     */
    public static HttpResult put(String url, String body) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);

        httpPut.addHeader("Content-Type", "application/json");
        try {
            httpPut.setEntity(new StringEntity(body));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return getResult(httpPut);
    }


    public static HttpResult post(String url, Map<String, Object> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    public static HttpResult postFace(String url, Map<String, String> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    public static HttpResult post(String url, Map<String, Object> headers, Map<String, Object> params)
            throws Exception {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    public static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {

        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    private static ArrayList<NameValuePair> covertParams(Map<String, String> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        params.forEach((k, v) -> {
            pairs.add(new BasicNameValuePair(k, v));
        });
        return pairs;
    }

    public static HttpResult delete(String url) throws Exception {
        HttpDelete httpDelete = new HttpDelete(url);
        return getResult(httpDelete);
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    public static HttpResult getResult(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();

        response.close();


        return HttpResult.builder()
                .json(EntityUtils.toString(entity))
                .statusCode(statusCode)
                .url(request.getURI().getPath())
                .build();
    }

    public static CloseableHttpResponse execute(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpClient = getHttpClient();
        return httpClient.execute(request);
    }

    @Data
    @Builder
    public static class HttpResult {
        private String json;
        private int statusCode;
        private String url;
    }

    /**
     * 存储头信息
     */
    protected Map<String, List<String>> headers = new HashMap<>();

    /**
     * 加入默认的头部信息
     *
     * @param isReset 是否重置所有头部信息（删除自定义保留默认）
     * @return this
     */
    public void putDefault(boolean isReset) {
        if (isReset) {
            this.headers.clear();
        }

        header(Header.ACCEPT, "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8", true);
        header(Header.ACCEPT_ENCODING, "gzip", true);
        header(Header.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8", true);
        header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded", true);
        header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36", true);
    }


    public void header(Header name, String value, boolean isOverride) {
        header(name.toString(), value, isOverride);
    }

    public void header(String name, String value, boolean isOverride) {
        if (null != name && null != value) {
            final List<String> values = headers.get(name.trim());
            if (isOverride || CollectionUtils.isEmpty(values)) {
                final ArrayList<String> valueList = new ArrayList<String>();
                valueList.add(value);
                headers.put(name.trim(), valueList);
            } else {
                values.add(value.trim());
            }
        }
    }

    /**
     * Http 头域
     */
    public enum Header {

        //------------------------------------------------------------- 通用头域
        /**
         * 提供日期和时间标志,说明报文是什么时间创建的
         */
        DATE("Date"),
        /**
         * 允许客户端和服务器指定与请求/响应连接有关的选项
         */
        CONNECTION("Connection"),
        /**
         * 给出发送端使用的MIME版本
         */
        MIME_VERSION("MIME-Version"),
        /**
         * 如果报文采用了分块传输编码(chunked transfer encoding) 方式,就可以用这个首部列出位于报文拖挂(trailer)部分的首部集合
         */
        TRAILER("Trailer"),
        /**
         * 告知接收端为了保证报文的可靠传输,对报文采用了什么编码方式
         */
        TRANSFER_ENCODING("Transfer-Encoding"),
        /**
         * 给出了发送端可能想要"升级"使用的新版本和协议
         */
        UPGRADE("Upgrade"),
        /**
         * 显示了报文经过的中间节点
         */
        VIA("Via"),
        /**
         * 指定请求和响应遵循的缓存机制
         */
        CACHE_CONTROL("Cache-Control"),
        /**
         * 用来包含实现特定的指令，最常用的是Pragma:no-cache。在HTTP/1.1协议中，它的含义和Cache- Control:no-cache相同
         */
        PRAGMA("Pragma"),
        /**
         * 请求表示提交内容类型或返回返回内容的MIME类型
         */
        CONTENT_TYPE("Content-Type"),

        //------------------------------------------------------------- 请求头域
        /**
         * 指定请求资源的Intenet主机和端口号，必须表示请求url的原始服务器或网关的位置。HTTP/1.1请求必须包含主机头域，否则系统会以400状态码返回
         */
        HOST("Host"),
        /**
         * 允许客户端指定请求uri的源资源地址，这可以允许服务器生成回退链表，可用来登陆、优化cache等。他也允许废除的或错误的连接由于维护的目的被 追踪。如果请求的uri没有自己的uri地址，Referer不能被发送。如果指定的是部分uri地址，则此地址应该是一个相对地址
         */
        REFERER("Referer"),
        /**
         * 指定请求的域
         */
        ORIGIN("Origin"),
        /**
         * HTTP客户端运行的浏览器类型的详细信息。通过该头部信息，web服务器可以判断到当前HTTP请求的客户端浏览器类别
         */
        USER_AGENT("User-Agent"),
        /**
         * 指定客户端能够接收的内容类型，内容类型中的先后次序表示客户端接收的先后次序
         */
        ACCEPT("Accept"),
        /**
         * 指定HTTP客户端浏览器用来展示返回信息所优先选择的语言
         */
        ACCEPT_LANGUAGE("Accept-Language"),
        /**
         * 指定客户端浏览器可以支持的web服务器返回内容压缩编码类型
         */
        ACCEPT_ENCODING("Accept-Encoding"),
        /**
         * 浏览器可以接受的字符编码集
         */
        ACCEPT_CHARSET("Accept-Charset"),
        /**
         * HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器
         */
        COOKIE("Cookie"),
        /**
         * 请求的内容长度
         */
        CONTENT_LENGTH("Content-Length"),

        //------------------------------------------------------------- 响应头域
        /**
         * Cookie
         */
        SET_COOKIE("Set-Cookie"),
        /**
         * Content-Encoding
         */
        CONTENT_ENCODING("Content-Encoding"),
        /**
         * Content-Disposition
         */
        CONTENT_DISPOSITION("Content-Disposition"),
        /**
         * ETag
         */
        ETAG("ETag"),
        /**
         * 重定向指示到的URL
         */
        LOCATION("Location");

        private String value;

        private Header(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
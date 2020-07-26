package cn.mirrorming.blog.utils.spider;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Mireal Chan
 */
@Component
@Slf4j
public class XiCiIpProxyParser {
    public static final String URL_PATTERN = "https://www.xicidaili.com/nn/{0}";
    private static final int READ_TIMEOUT = 10;
    private static final int CONNECT_TIMEOUT = 10;
    private static final int WRITE_TIMEOUT = 10;

    public boolean testIp(String ip, int port) throws Exception {
        String url = "https://www.baidu.com";

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // 读取超时
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                // 连接超时
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port)))
                .build();
        // 构造 Request
        Request request = new Request.Builder().get().url(url).build();
        // 将 Request 封装为 Call
        okhttp3.Call call = okHttpClient.newCall(request);
        // 执行 Call，得到 Response
        int code = call.execute().code();
        return code == 200;
    }

    public void parse(String id) throws IOException {
        String url = MessageFormat.format(URL_PATTERN, id);
        Connection.Response response = null;
        while (response == null) {
            response = JsoupUtil.getResponse(url);
        }
        Document document = response.parse();
        List<String> collect = document.select("#ip_list > tbody > tr")
                .stream()
                .skip(1)
                .map(item -> item.select("td:nth-child(2)").text() + ":" + item.select("td:nth-child(3)").text())
                .collect(Collectors.toList());
        collect.parallelStream().forEach(c -> {
            String[] split = c.split(":");
            try {
                if (testIp(split[0], Integer.parseInt(split[1]))) {
                    System.out.println(c);
                    log.info("ip----------------有效---------------->{}", c);
                }
            } catch (Exception e) {
                log.warn("ip无效，{}", c);
            }
        });

    }
}

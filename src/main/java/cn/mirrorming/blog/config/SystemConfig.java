package cn.mirrorming.blog.config;

import cn.mirrorming.blog.utils.HttpClientUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mireal
 * @version V1.0
 * @date 2019/12/26 16:11
 */
@Configuration
public class SystemConfig {
    private final static int TIMEOUT = 5000;

    @Bean
    public RestTemplate httpsRestTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplate httpsRestTemplate = builder.build();
        httpsRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientUtils.createHttpsClient(TIMEOUT)));
        return httpsRestTemplate;
    }
}

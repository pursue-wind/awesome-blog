package cn.mirrorming.blog;

import cn.mirrorming.blog.config.properties.SecurityProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Mireal
 */
@EnableConfigurationProperties(SecurityProperties.class)
@MapperScan(basePackages = "cn.mirrorming.blog.mapper")
@SpringBootApplication
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
